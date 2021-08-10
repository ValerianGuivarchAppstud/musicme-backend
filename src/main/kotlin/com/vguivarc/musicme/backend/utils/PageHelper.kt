package com.vguivarc.musicme.backend.utils

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

object PageHelper {
    /**
     * build a Page response from a list, taking in account the pageable of the initial request
     * @param list List<T>
     * @param pageable Pageable
     * @return Page<T>
     */
    fun <T> buildPageResponseBy(list: List<T>, pageable: Pageable): Page<T> {
        val pageNumber = pageable.pageNumber
        val pageSize = pageable.pageSize
        val totalSize = list.size
        val offset = pageNumber * pageSize
        val newList = if (offset > totalSize) {
            emptyList()
        } else {
            val limit = if (totalSize < offset + pageSize) totalSize - offset else pageSize
            list.subList(offset, offset + limit)
        }
        return PageImpl(newList, pageable, totalSize.toLong())
    }
}

/**
 * this allows to convert sort parameters between the api and data namings
 * @receiver Pageable
 * @return Pageable
 */
fun Pageable.convertSortFields(): Pageable {
    val entityDate = this.sort.getOrderFor("createdDate")

    return if (entityDate != null) {
        PageRequest.of(
            this.pageNumber,
            this.pageSize,
            Sort.by(entityDate.direction, "created_date")
        )
    } else {
        this
    }
}
