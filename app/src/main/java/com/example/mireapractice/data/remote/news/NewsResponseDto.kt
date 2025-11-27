package com.example.mireapractice.data.remote.news

data class NewsResponseDto(
    val rows: Int?,
    val os: Int?,
    val page: Int?,
    val total: Int?,
    val documents: Map<String, NewsDocumentDto>?
)

data class NewsDocumentDto(
    val id: String?,
    val displayTitle: String?,
    val pdfUrl: String?,
    val url: String?
)