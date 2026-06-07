package com.example.tourismguide.domain.repository

import com.example.tourismguide.data.local.entity.ReviewEntity
import kotlinx.coroutines.flow.Flow

interface ReviewRepository {
    fun getReviews(targetId: String, targetType: String): Flow<List<ReviewEntity>>
    fun getMyReviews(authorId: String): Flow<List<ReviewEntity>>
}
