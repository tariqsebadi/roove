package com.mmdev.roove.core


import com.mmdev.business.auth.repository.AuthRepository
import com.mmdev.business.auth.usecase.*
import com.mmdev.business.cards.repository.CardsRepository
import com.mmdev.business.cards.usecase.AddToSkippedUseCase
import com.mmdev.business.cards.usecase.GetMatchedUsersUseCase
import com.mmdev.business.cards.usecase.GetPotentialUsersUseCase
import com.mmdev.business.cards.usecase.HandlePossibleMatchUseCase
import com.mmdev.business.chat.repository.ChatRepository
import com.mmdev.business.chat.usecase.GetMessagesUseCase
import com.mmdev.business.chat.usecase.SendMessageUseCase
import com.mmdev.business.chat.usecase.SendPhotoUseCase
import com.mmdev.business.chat.usecase.SetConversationUseCase
import com.mmdev.business.conversations.repository.ConversationsRepository
import com.mmdev.business.conversations.usecase.CreateConversationUseCase
import com.mmdev.business.conversations.usecase.DeleteConversationUseCase
import com.mmdev.business.conversations.usecase.GetConversationsListUseCase
import com.mmdev.business.user.repository.UserRepository
import com.mmdev.business.user.usecase.local.GetSavedUserUseCase
import com.mmdev.business.user.usecase.local.SaveUserInfoUseCase
import com.mmdev.business.user.usecase.remote.CreateUserUseCase
import com.mmdev.business.user.usecase.remote.DeleteUserUseCase
import com.mmdev.business.user.usecase.remote.GetUserByIdUseCase
import com.mmdev.roove.ui.actions.conversations.viewmodel.ConversationsViewModelFactory
import com.mmdev.roove.ui.auth.viewmodel.AuthViewModelFactory
import com.mmdev.roove.ui.cards.viewmodel.CardsViewModelFactory
import com.mmdev.roove.ui.chat.viewmodel.ChatViewModelFactory
import com.mmdev.roove.ui.main.viewmodel.local.LocalUserRepoVMFactory
import com.mmdev.roove.ui.main.viewmodel.remote.RemoteUserRepoVMFactory
import dagger.Module
import dagger.Provides

@Module
class ViewModelModule {

	@Provides
	fun authViewModelFactory(repository: AuthRepository): AuthViewModelFactory{
		return AuthViewModelFactory(HandleUserExistenceUseCase(repository),
		                            IsAuthenticatedUseCase(repository),
		                            LogOutUseCase(repository),
		                            SignInWithFacebookUseCase(repository),
		                            SignUpUseCase(repository))
	}

	@Provides
	fun cardsViewModelFactory(repository: CardsRepository): CardsViewModelFactory{
		return CardsViewModelFactory(AddToSkippedUseCase(repository),
		                             GetMatchedUsersUseCase(repository),
		                             GetPotentialUsersUseCase(repository),
		                             HandlePossibleMatchUseCase(repository))
	}

	@Provides
	fun chatViewModelFactory(repository: ChatRepository): ChatViewModelFactory {
		return ChatViewModelFactory(GetMessagesUseCase(repository),
		                            SendMessageUseCase(repository),
		                            SendPhotoUseCase(repository),
		                            SetConversationUseCase(repository))
	}

	@Provides
	fun conversationsViewModelFactory(repository: ConversationsRepository): ConversationsViewModelFactory {
		return ConversationsViewModelFactory(CreateConversationUseCase(repository),
		                                     DeleteConversationUseCase(repository),
		                                     GetConversationsListUseCase(repository))
	}

	@Provides
	fun localUserRepoVMFactory(repository: UserRepository.LocalUserRepository): LocalUserRepoVMFactory {
		return LocalUserRepoVMFactory(GetSavedUserUseCase(
				repository), SaveUserInfoUseCase(repository))
	}


	@Provides
	fun remoteUserRepoVMFactory(repository: UserRepository.RemoteUserRepository): RemoteUserRepoVMFactory {
		return RemoteUserRepoVMFactory(CreateUserUseCase(
				repository), DeleteUserUseCase(repository), GetUserByIdUseCase(repository))
	}

}