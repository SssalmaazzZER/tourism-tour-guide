package com.example.tourismguide.data.repository

import com.example.tourismguide.data.local.dao.ReviewDao
import com.example.tourismguide.data.local.entity.ReviewEntity
import com.example.tourismguide.domain.repository.ReviewRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ReviewRepositoryImpl @Inject constructor(
    private val reviewDao: ReviewDao
) : ReviewRepository {
    override fun getReviews(targetId: String, targetType: String): Flow<List<ReviewEntity>> =
        reviewDao.observeForTarget(targetId, targetType)

    override fun getMyReviews(authorId: String): Flow<List<ReviewEntity>> =
        reviewDao.observeByAuthor(authorId)
}
