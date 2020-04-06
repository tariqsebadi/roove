/*
 * Created by Andrii Kovalchuk
 * Copyright (c) 2020. All rights reserved.
 * Last modified 06.04.20 15:01
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.mmdev.roove.ui.dating.cards.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.lifecycle.Observer
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mmdev.business.core.UserItem
import com.mmdev.roove.R
import com.mmdev.roove.databinding.FragmentCardsBinding
import com.mmdev.roove.ui.common.ImagePagerAdapter
import com.mmdev.roove.ui.common.base.BaseFragment
import com.mmdev.roove.ui.dating.cards.CardsViewModel
import com.mmdev.roove.ui.dating.cards.CardsViewModel.TwoCardsModel
import com.mmdev.roove.ui.profile.view.PlacesToGoAdapter
import kotlinx.android.synthetic.main.fragment_cards.*


class CardsFragment: BaseFragment<CardsViewModel>() {

	private val mPlacesToGoAdapter = PlacesToGoAdapter()
	private val mTopImagePagerAdapter = ImagePagerAdapter()
	private val mBottomImagePagerAdapter = ImagePagerAdapter()


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		associatedViewModel = getViewModel()
		associatedViewModel.loadUsersByPreferences(initialLoading = true)


		associatedViewModel.showMatchDialog.observe(this, Observer {
			//if (it) showMatchDialog(mDisappearedUserItem)
		})

		associatedViewModel
			.stream
			.observe(this, Observer {
				bindCards(it)
			})
	}

	private fun bindCards(twoCardsModel: TwoCardsModel) {
		//top card ui
		tvTopCardUserName.text = twoCardsModel.top.baseUserInfo.name
		mTopImagePagerAdapter.setData(twoCardsModel.top.photoURLs.map { photo -> photo.fileUrl })


		//bottom card ui
		tvBottomCardUserName.text = twoCardsModel.bottom.baseUserInfo.name
		mBottomImagePagerAdapter.setData(twoCardsModel.bottom.photoURLs.map { photo -> photo.fileUrl })
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?) =
		FragmentCardsBinding.inflate(inflater, container, false)
			.apply {
				lifecycleOwner = this@CardsFragment
				viewModel = associatedViewModel
				executePendingBindings()
			}
			.root

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

		coreMotionLayout.setTransitionListener(object : MotionLayout.TransitionListener {
			override fun onTransitionTrigger(p0: MotionLayout, p1: Int, p2: Boolean, p3: Float) {}
			override fun onTransitionStarted(p0: MotionLayout, p1: Int, p2: Int) {}
			override fun onTransitionChange(p0: MotionLayout, p1: Int, p2: Int, p3: Float) {}
			override fun onTransitionCompleted(motionLayout: MotionLayout, currentId: Int) {
				when (currentId) {
					R.id.offScreenPass -> {
						motionLayout.progress = 0f
						motionLayout.setTransition(R.id.rest, R.id.pass)
						associatedViewModel.swipe()
					}
					R.id.offScreenLike -> {
						motionLayout.progress = 0f
						motionLayout.setTransition(R.id.rest, R.id.like)
						associatedViewModel.swipe()
					}
				}
			}
		})

		/*
		* top card setup ui
		*/
		viewPagerTopCardPhotoList.apply {
			adapter = mTopImagePagerAdapter
			isUserInputEnabled = false
		}

		TabLayoutMediator(tlTopCardPhotosIndicator, viewPagerTopCardPhotoList) {
			_: TabLayout.Tab, _: Int -> //do nothing
		}.attach()

		topCardNextImage.setOnClickListener {
			if (viewPagerTopCardPhotoList.currentItem != viewPagerTopCardPhotoList.itemDecorationCount - 1)
				viewPagerTopCardPhotoList.currentItem++
		}
		topCardPreviousImage.setOnClickListener {
			if (viewPagerTopCardPhotoList.currentItem != 0) viewPagerTopCardPhotoList.currentItem--
		}


		/*
		* bottom card setup ui
		*/
		viewPagerBottomCardPhotoList.apply {
			adapter = mBottomImagePagerAdapter
			isUserInputEnabled = false
		}

		TabLayoutMediator(tlBottomCardPhotosIndicator, viewPagerBottomCardPhotoList) {
			_: TabLayout.Tab, _: Int -> //do nothing
		}.attach()

		bottomCardNextImage.setOnClickListener {
			if (viewPagerBottomCardPhotoList.currentItem != viewPagerBottomCardPhotoList.itemDecorationCount - 1)
				viewPagerBottomCardPhotoList.currentItem++
		}
		bottomCardPreviousImage.setOnClickListener {
			if (viewPagerBottomCardPhotoList.currentItem != 0) viewPagerBottomCardPhotoList.currentItem--
		}


//		cardContainer.setTransitionListener(object : MotionLayout.TransitionListener{
//			override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {}
//
//			override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
//				viewPagerClickableDispatcher.isClickable = false
//			}
//
//			override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {}
//
//			override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
//				viewPagerClickableDispatcher.isClickable = true
//			}
//		})

//		viewPagerCardPhotoList.apply {
//			adapter = mImagePagerAdapter
//			isUserInputEnabled = false
//		}
//
//		TabLayoutMediator(tlCardPhotosIndicator, viewPagerCardPhotoList) { _: TabLayout.Tab, _: Int ->
//			//do nothing
//		}.attach()
//
//		nextImage.setOnClickListener {
//			if (viewPagerCardPhotoList.currentItem != viewPagerCardPhotoList.itemDecorationCount - 1)
//				viewPagerCardPhotoList.currentItem++
//		}
//		previousImage.setOnClickListener {
//			if (viewPagerCardPhotoList.currentItem != 0)
//				viewPagerCardPhotoList.currentItem--
//		}
//
//		rvProfileWantToGoList.apply {
//			adapter = mPlacesToGoAdapter
//		}


	}

	private fun showMatchDialog(userItem: UserItem) {
		val dialog = MatchDialogFragment.newInstance(userItem.baseUserInfo.name,
		                                             userItem.baseUserInfo.mainPhotoUrl)

		dialog.show(childFragmentManager, MatchDialogFragment::class.java.canonicalName)
	}

}
