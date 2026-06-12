package com.example.tourismguide.domain.model

data class ContentRichDetail(
    val contentId: String,
    val richBody: String,
    val galleryUrls: List<String>,
    val leaders: List<ContentLeader>
)

data class ContentLeader(
    val name: String,
    val role: String,
    val bio: String,
    val imageUrl: String
)
