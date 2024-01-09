package com.clean.mvvm.domain.usecase.catsDetail

import com.clean.mvvm.data.NetworkResult
import com.clean.mvvm.domain.mappers.CallSuccessModel
import com.clean.mvvm.domain.mappers.mapSuccessData
import com.clean.mvvm.domain.repositories.CatDetailsRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class DeleteFavCatUseCaseImpl(private val catDetailsRepo: CatDetailsRepository) :
    DeleteFavCatUseCase {
    override suspend fun execute(imageId: String, favId: Int) =
        flow<NetworkResult<CallSuccessModel>> {
            emit(NetworkResult.Loading())
            with(catDetailsRepo.deleteFavouriteCatApi(favId)) {
                if (isSuccessful) {
                    emit(NetworkResult.Success(this.body()?.mapSuccessData()))
                    catDetailsRepo.deleteFavouriteCatLocal(imageId)
                } else {
                    emit(NetworkResult.Error(this.errorBody().toString()))
                }
            }

        }.catch {
            emit(NetworkResult.Error(it.localizedMessage))
        }

}

