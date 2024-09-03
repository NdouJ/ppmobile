package hr.algebra.pawprotectormobile.model

data class Review(
    val idUserReview: Int,
    val grade: Int,
    val review: String,
    val userId: Int,
    val breederId: Int
)
