package com.vguivarc.musicme.backend.web.api.v1.admin.account


import com.vguivarc.musicme.backend.domain.services.AccountService
import com.vguivarc.musicme.backend.web.api.v1.admin.account.entities.BoAccount
import com.vguivarc.musicme.backend.web.api.v1.admin.account.requests.BoAccountCreate
import com.vguivarc.musicme.backend.web.api.v1.admin.account.requests.BoAccountUpdate
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@CrossOrigin
@RequestMapping("/api/v1/admin/accounts")
@RestController
@Api(value = "admin-accounts")
class AdminAccountController {

    @Autowired
    lateinit var accountService: AccountService

    @Suppress("LongParameterList") // this is intentional
    @GetMapping
    @ApiOperation(value = "admin > accounts > get all")
    fun getAll(
        @RequestParam("qOrganizationProfile")
        @ApiParam(required = false)
        qOrganizationProfile: String?,
        @RequestParam("qEmail")
        @ApiParam(required = false)
        qEmail: String?,
        @RequestParam("qId")
        @ApiParam(required = false)
        qId: String?,
        @RequestParam("qAuthority")
        @ApiParam(required = false)
        qAuthority: String?,
        @RequestParam("qStatus")
        @ApiParam(required = false)
        qStatus: String?,
        p: Pageable
    ): Page<BoAccount> {
        val accounts = when {
            qEmail != null -> {
                accountService.findAllByEmailContains(qEmail, p)
            }
            qId != null -> {
                accountService.findAllWithId(listOf(qId), p)
            }
            qStatus != null -> {
                accountService.findAllByStatusContains(qStatus, p)
            }
            qAuthority != null -> {
                accountService.findAllByAuthorityContains(qAuthority, p)
            }
            else -> {
                accountService.findAll(p)
            }
        }
        return accounts.map { BoAccount.fromAccount(it) }
    }

    @GetMapping("/many")
    @ApiOperation(value = "admin > accounts > get many with ids")
    fun getMany(
        @RequestParam("ids[]")
        @ApiParam(value = "the id of the accounts", required = true)
        ids: Array<String>,
        p: Pageable
    ): Page<BoAccount> {
        return accountService.findAllWithId(ids.toList(), p).map { BoAccount.fromAccount(it) }
    }

    @GetMapping("/manyReferences")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "admin > accounts > get many by reference")
    fun getManyReferences(
        @RequestParam("id")
        @ApiParam(value = "the id of the account", required = true)
        id: String?
    ): Page<BoAccount> {
        return id?.let {
            PageImpl(listOf(BoAccount.fromAccount(accountService.findById(id))))
        } ?: PageImpl(listOf())
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "admin > accounts > get one")
    fun getOne(
        @PathVariable("id")
        @ApiParam(value = "the id of the account", required = true)
        id: String,
        p: Pageable
    ): BoAccount {
        val account = accountService.findById(id)
        return BoAccount.fromAccount(account)
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "admin > accounts > update one")
    fun update(
        @PathVariable("id")
        @ApiParam(value = "the id of the account", required = true)
        id: String,
        @Valid
        @RequestBody
        @ApiParam(value = "the request to update the account", required = true)
        boAccountUpdate: BoAccountUpdate
    ): BoAccount {
        val account = accountService.findById(id)
        return BoAccount.fromAccount(accountService.updateAccount(account, boAccountUpdate.toAccount()))
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "admin > accounts > create")
    fun create(
        @Valid
        @RequestBody
        @ApiParam(value = "the request to update the account", required = true)
        boAccountCreate: BoAccountCreate
    ): BoAccount {
        return BoAccount.fromAccount(
            accountService.createAccount(
                account = boAccountCreate.toAccount()
            )
        )
    }
}
