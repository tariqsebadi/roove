/*
 * Created by Andrii Kovalchuk
 * Copyright (c) 2020. All rights reserved.
 * Last modified 03.04.20 18:43
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.mmdev.roove.ui.dating.cards.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mmdev.business.core.UserItem
import com.mmdev.roove.R
import com.mmdev.roove.databinding.FragmentCardsItemBinding
import com.mmdev.roove.ui.common.ImagePagerAdapter

class CardsStackAdapter (private var usersList: List<UserItem> = emptyList()):
		RecyclerView.Adapter<CardsStackAdapter.CardsViewHolder>() {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
		CardsViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context),
		                                        R.layout.fragment_cards_item,
		                                        parent,
		                                        false))


	override fun onBindViewHolder(holderCards: CardsViewHolder, position: Int) =
		holderCards.bind(usersList[position])


	override fun getItemCount() = usersList.size

	fun getItem(position: Int) = usersList[position]

	fun setData(newCardItems: List<UserItem>) {
		usersList = newCardItems
		notifyDataSetChanged()
	}

	inner class CardsViewHolder (private val binding: FragmentCardsItemBinding):
			RecyclerView.ViewHolder(binding.root) {

		private val vp = itemView.findViewById<ViewPager2>(R.id.viewPagerCardPhotoList)
		private val next = itemView.findViewById<View>(R.id.nextImage)
		private val previous = itemView.findViewById<View>(R.id.previousImage)
		private val tabIndicator = itemView.findViewById<TabLayout>(R.id.tlCardPhotosIndicator)
		private val cardContainer = itemView.findViewById<MotionLayout>(R.id.cardContainer)

		/*
		*   executePendingBindings()
		*   Evaluates the pending bindings,
		*   updating any Views that have expressions bound to modified variables.
		*   This must be run on the UI thread.
		*/
		fun bind(userItem: UserItem){
			binding.bindItem = userItem
			// prevent to click buttons during transitions
			cardContainer.setTransitionListener(object : MotionLayout.TransitionListener {
				override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {}
				override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
					vp.isClickable = false
				}
				override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {}
				override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
					vp.isClickable = true
				}
			})

			vp.apply {
				adapter = ImagePagerAdapter()
				isUserInputEnabled = false
			}

			TabLayoutMediator(tabIndicator, vp) { _: TabLayout.Tab, _: Int ->
				//do nothing
			}.attach()

			next.setOnClickListener { if (vp.currentItem != userItem.photoURLs.size - 1) vp.currentItem++ }
			previous.setOnClickListener { if (vp.currentItem != 0) vp.currentItem-- }

			binding.executePendingBindings()
		}

	}

}