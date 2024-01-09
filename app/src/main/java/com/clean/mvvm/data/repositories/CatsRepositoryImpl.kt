package com.clean.mvvm.data.repositories

import com.clean.mvvm.data.NetworkResult
import com.clean.mvvm.data.models.catData.FavouriteCatsItem
import com.clean.mvvm.data.services.cats.CatApiServiceHelper
import com.clean.mvvm.data.services.cats.CatsDatabaseHelper
import com.clean.mvvm.domain.repositories.CatsRepository

import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class CatsRepositoryImpl(
    private val catsApiService: CatApiServiceHelper,
    private val catsDatabaseHelper: CatsDatabaseHelper
) : CatsRepository {
    override suspend fun fetchCats(limit: Int) = catsApiService.fetchCatsImages(limit)

    override suspend fun fetchFavouriteCats(subId: String) =
        catsApiService.fetchFavouriteCats(subId)

    override suspend fun insertFavouriteCats(data: List<FavouriteCatsItem>): List<Long> =
        catsDatabaseHelper.insertFavCatImageRelation(data)


    override suspend fun fetchTestFavouriteCats(subId: String) =
        flow<NetworkResult<List<FavouriteCatsItem>>> {
            emit(NetworkResult.Loading())
            with(catsApiService.fetchFavouriteCats(subId)) {
                if (isSuccessful) {
                    emit(NetworkResult.Success(this.body()))
                } else {
                    emit(NetworkResult.Error(this.errorBody().toString()))
                }
            }
        }.catch {
            emit(NetworkResult.Error(it.localizedMessage))
        }


}