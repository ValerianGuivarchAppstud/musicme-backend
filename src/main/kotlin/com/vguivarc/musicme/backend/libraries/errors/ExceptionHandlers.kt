package com.vguivarc.musicme.backend.libraries.errors

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.MessageSource
import org.springframework.context.MessageSourceAware
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.core.convert.ConversionFailedException
import org.springframework.dao.ConcurrencyFailureException
import org.springframework.data.repository.query.ParameterOutOfBoundsException
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.core.AuthenticationException
import org.springframework.validation.BindException
import org.springframework.validation.FieldError
import org.springframework.web.HttpMediaTypeNotAcceptableException
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingPathVariableException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.multipart.MultipartException
import org.springframework.web.multipart.support.MissingServletRequestPartException
import org.springframework.web.servlet.NoHandlerFoundException
import java.nio.file.AccessDeniedException
import java.util.Arrays
import java.util.Locale
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@ConditionalOnProperty("application.libraries.errors.enabled")
@ControllerAdvice
@Suppress("TooManyFunctions")
class ExceptionHandlers : MessageSourceAware {

    private val logger = LoggerFactory.getLogger(ExceptionHandlers::class.java)

    companion object {
        fun respond(req: HttpServletRequest?, response: HttpServletResponse, ex: DomainException) {
            val builder = ResponseEntity.status(ex.statusCode)
            val path = req?.servletPath
            ex.headers?.forEach { header ->
                builder.headers(header)
            }

            val errorVM = ErrorVM(path, ex, ex.message.orEmpty())

            response.status = ex.statusCode.value()
            val json = ObjectMapper().writeValueAsString(errorVM)
            response.writer.write(json)
            response.contentType = MediaType.APPLICATION_JSON.toString()
            response.flushBuffer()
        }
    }

    init {
        logger.info("Adding exception handling to API.")
    }

    lateinit var givenMessageSource: MessageSource

    override fun setMessageSource(messageSource: MessageSource) {
        this.givenMessageSource = messageSource
    }

    private fun localize(ex: DomainException): String {
        return givenMessageSource.getMessage(
            "api.errors.types." + ex.type,
            ex.details.toTypedArray(),
            ex.message,
            Locale.FRANCE
        ) ?: ""
    }

    /**
     * Build the final returned request
     *
     * @param req Current request
     * @param ex Exception
     * @return ErrorVM
     */
    private fun build(req: HttpServletRequest?, ex: DomainException): ResponseEntity<ErrorVM> {
        val builder = ResponseEntity.status(ex.statusCode)
        val path = req?.servletPath
        ex.headers?.forEach { header ->
            builder.headers(header)
        }
        return builder.body(ErrorVM(path, ex, localize(ex)))
    }

    private fun addTypeInformations(exception: DomainException, type: Class<*>, name: String, value: Any) {
        if (type.isEnum) {
            val values = Arrays.toString(type.enumConstants)
            val defaultMessage = "Value [ $value ] is not valid. Accepted values: $values"
            val message = givenMessageSource.getMessage(
                "api.errors.constaints.enum",
                listOf(value.toString(), values).toTypedArray(),
                defaultMessage,
                Locale.FRANCE
            )
            exception.field(FieldError("self", name, message ?: ""))
        }
    }

    /**
     * Handlers
     */
    @ExceptionHandler(ConcurrencyFailureException::class)
    fun processConcurrencyError(req: HttpServletRequest, ex: ConcurrencyFailureException): ResponseEntity<*> {
        return build(req, DomainException(BaseExceptions.CONFLICT_ERROR))
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun processValidationError(req: HttpServletRequest, ex: MethodArgumentNotValidException): ResponseEntity<*> {
        val res = DomainException(BaseExceptions.VALIDATION_FAILURE)
        val bindingResult = ex.bindingResult

        for (fe in bindingResult.fieldErrors) {
            res.field(fe)
        }

        return build(req, res)
    }

    @ExceptionHandler(MissingPathVariableException::class)
    fun processPathPathMissing(req: HttpServletRequest, ex: MissingPathVariableException): ResponseEntity<*> {
        val res = DomainException(BaseExceptions.VALIDATION_FAILURE)
        ex.message.let { res.add(it) }
        return build(req, res)
    }

    @ExceptionHandler(MissingServletRequestParameterException::class)
    fun processServletRequestMissingException
    (req: HttpServletRequest, ex: MissingServletRequestParameterException): ResponseEntity<*> {
        val res = DomainException(BaseExceptions.VALIDATION_FAILURE)

        ex.message.let { res.add(it) }
        return build(req, res)
    }

    @ExceptionHandler(AuthenticationException::class)
    fun processAuthenticationException
    (req: HttpServletRequest, e: AuthenticationException): ResponseEntity<*> {
        val res = DomainException(BaseExceptions.ACCESS_DENIED)
        e.message?.let { res.add(it) }
        return build(req, res)
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun processAccessDeniedException
    (req: HttpServletRequest, e: AccessDeniedException): ResponseEntity<*> {
        val res = DomainException(BaseExceptions.ACCESS_DENIED)
        e.message?.let { res.add(it) }
        return build(req, res)
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun processMethodNotSupportedException
    (req: HttpServletRequest, exception: HttpRequestMethodNotSupportedException): ResponseEntity<*> {
        return build(req, DomainException(BaseExceptions.METHOD_NOT_ALLOWED))
    }

    @ExceptionHandler(NoHandlerFoundException::class)
    fun resourceNotFoundException(req: HttpServletRequest, exception: NoHandlerFoundException): ResponseEntity<*> {
        return build(req, DomainException(BaseExceptions.RESOURCE_NOT_FOUND))
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException::class)
    fun mediaTypeNotAcceptable
    (req: HttpServletRequest, exception: HttpMediaTypeNotAcceptableException): ResponseEntity<*> {
        return build(req, DomainException(BaseExceptions.METHOD_NOT_ALLOWED))
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun processNotReadableException
    (req: HttpServletRequest, ex: HttpMessageNotReadableException): ResponseEntity<*> {
        val exception = DomainException(BaseExceptions.VALIDATION_FAILURE)

        if (ex.cause is InvalidFormatException) {
            val formatException = ex.cause as InvalidFormatException
            addTypeInformations(
                exception, formatException.targetType,
                formatException.pathReference, formatException.value
            )
        }

        ex.mostSpecificCause.message?.let {
            exception.add(it)
        }

        return build(req, exception)
    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException::class)
    fun processSpringDeniedException(
        req: HttpServletRequest,
        exception: org.springframework.security.access.AccessDeniedException
    ): ResponseEntity<*> {
        return build(req, DomainException(BaseExceptions.ACCESS_DENIED))
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException::class)
    fun processSpringDeniedException
    (req: HttpServletRequest, exception: HttpMediaTypeNotSupportedException): ResponseEntity<*> {
        return build(req, DomainException(BaseExceptions.MEDIA_TYPE_NOT_SUPPORTED))
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun processConversionFailedException
    (req: HttpServletRequest, ex: MethodArgumentTypeMismatchException): ResponseEntity<*> {
        val res = DomainException(BaseExceptions.VALIDATION_FAILURE)

        if (ex.cause is ConversionFailedException) {
            // not sure about defaults values
            addTypeInformations(res, ex.requiredType ?: String().javaClass, ex.name, ex.value ?: "unknown")
        }

        ex.mostSpecificCause.message?.let {
            res.add(it)
        }

        return build(req, res)
    }

    @ExceptionHandler(MultipartException::class)
    fun processMultipartException(req: HttpServletRequest, ex: MultipartException): ResponseEntity<*> {
        return build(req, DomainException(BaseExceptions.VALIDATION_FAILURE))
    }

    @ExceptionHandler(MissingServletRequestPartException::class)
    fun processMissingServletRequestPartException
    (req: HttpServletRequest, ex: MissingServletRequestPartException): ResponseEntity<*> {
        val res = DomainException(BaseExceptions.VALIDATION_FAILURE)
        res.field(FieldError("self", ex.requestPartName, ex.message ?: ""))
        return build(req, res)
    }

    @ExceptionHandler(BindException::class)
    fun processBindException(req: HttpServletRequest, ex: BindException): ResponseEntity<*> {
        val res = DomainException(BaseExceptions.VALIDATION_FAILURE)
        for (error in ex.fieldErrors) {
            res.field(error)
        }
        return build(req, res)
    }

    /**
     * Backend dev errors
     */

    @ExceptionHandler(ParameterOutOfBoundsException::class)
    fun processOutOfBounds(req: HttpServletRequest, ex: ParameterOutOfBoundsException): ResponseEntity<*> {
        val res = DomainException(BaseExceptions.FATAL_SERVER_ERROR)
        return build(req, res)
    }

    /**
     * Generic handlers
     */

    @ExceptionHandler(DomainException::class)
    fun processException(req: HttpServletRequest, ex: DomainException): ResponseEntity<*> {
        return build(req, ex)
    }

    @ExceptionHandler(Exception::class)
    fun processRuntimeException(req: HttpServletRequest, ex: Exception): ResponseEntity<*> {
        val errorVM: DomainException
        val responseStatus = AnnotationUtils.findAnnotation(ex.javaClass, ResponseStatus::class.java)
        if (responseStatus != null) {
            errorVM = DomainException("ERROR." + responseStatus.value.value(), responseStatus.reason)
            errorVM.statusCode = responseStatus.value
        } else {
            logger.error("Internal error happened", ex)
            errorVM = DomainException(BaseExceptions.INTERNAL_SERVER_ERROR)
        }
        return build(req, errorVM)
    }
}
