/*
 * Created by Andrii Kovalchuk
 * Copyright (c) 2020. All rights reserved.
 * Last modified 16.03.20 15:42
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.mmdev.roove.ui.dating.chat.view


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mmdev.business.chat.entity.MessageItem
import com.mmdev.roove.R
import com.mmdev.roove.core.glide.GlideApp
import java.text.SimpleDateFormat
import java.util.*

class ChatAdapter (private var listMessageItems: List<MessageItem>):
	RecyclerView.Adapter<ChatAdapter.ChatViewHolder>(){

	private lateinit var attachedPhotoClickListener: OnItemClickListener
	private var userId = ""

	companion object {
		private const val RIGHT_MSG = 0
		private const val LEFT_MSG = 1
		private const val RIGHT_MSG_IMG = 2
		private const val LEFT_MSG_IMG = 3
	}


	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
		if (viewType == RIGHT_MSG || viewType == RIGHT_MSG_IMG)
			ChatViewHolder(LayoutInflater.from(parent.context)
				               .inflate(R.layout.fragment_chat_item_right,
				                        parent,
				                        false))
			else ChatViewHolder(LayoutInflater.from(parent.context)
				.inflate(R.layout.fragment_chat_item_left,
				         parent,
				         false))

	override fun onBindViewHolder(viewHolder: ChatViewHolder, position: Int) {
		viewHolder.setMessageType(getItemViewType(position))
		viewHolder.bind(listMessageItems[position])
	}

	override fun getItemViewType(position: Int): Int {
		val message = listMessageItems[position]
		return if (message.photoItem != null) {
			if (message.sender.userId == userId) RIGHT_MSG_IMG
			else LEFT_MSG_IMG
		}
		else {
			if (message.sender.userId == userId) RIGHT_MSG
			else LEFT_MSG
		}
	}

	override fun getItemCount() = listMessageItems.size

	fun getItem(position: Int) = listMessageItems[position]

	fun setCurrentUserId(id: String) { userId = id }

	fun updateData(newMessagesItems: List<MessageItem>) {
		listMessageItems = newMessagesItems
		notifyDataSetChanged()
	}

	/* note: USE FOR -DEBUG ONLY */
//	fun changeSenderName(name:String){
//		userId = name
//	}

	// allows clicks events on attached photo
	fun setOnAttachedPhotoClickListener(itemClickListener: OnItemClickListener) {
		attachedPhotoClickListener = itemClickListener
	}

	inner class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view){

		private val tvTextMessage: TextView = itemView.findViewById(R.id.tvChatMessageText)
		private val tvTimestamp: TextView = itemView.findViewById(R.id.tvChatMessageTimestamp)
		private val ivChatPhoto: ImageView = itemView.findViewById(R.id.ivChatMessagePhoto)

		init {
			ivChatPhoto.setOnClickListener {
				attachedPhotoClickListener.onItemClick(itemView.rootView, adapterPosition)
			}
		}

		fun setMessageType(messageType: Int) {
			when (messageType) {
				RIGHT_MSG -> ivChatPhoto.visibility = View.GONE
				LEFT_MSG -> ivChatPhoto.visibility = View.GONE
				RIGHT_MSG_IMG -> tvTextMessage.visibility = View.GONE
				LEFT_MSG_IMG -> tvTextMessage.visibility = View.GONE
			}
		}

		fun bind(messageItem: MessageItem) {
			setTextMessage(messageItem.text)
			messageItem.timestamp?.let {
				try {
					setTvTimestamp(convertTimestamp(it as Date))
				}catch (e: ClassCastException) {
					setTvTimestamp("")
				}
			}
			messageItem.photoItem?.let { setIvChatPhoto(it.fileUrl) }
		}

		/* sets text message in TxtView binded layout */
		private fun setTextMessage(message: String?) { tvTextMessage.text = message }

		/* set timestamp in TxtView located below message with time when this message was sent */
		private fun setTvTimestamp(timestamp: String) { tvTimestamp.text = timestamp }

		/* set photo that user sends in chat */
		private fun setIvChatPhoto(url: String) {
			if (url.isNotEmpty()) {
				GlideApp.with(ivChatPhoto.context)
					.load(url)
					.diskCacheStrategy(DiskCacheStrategy.RESOURCE)
					.into(ivChatPhoto)
			}
		}

	}

	/**
	 * parsing timestamp to display in traditional format
	 * @param date timestamp made by firestore
	 * @return string in format hh:mm AM/PM
	 */
	private fun convertTimestamp(date: Date) = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(date)


	// parent fragment will override this method to respond to click events
	interface OnItemClickListener {
		fun onItemClick(view: View, position: Int)
	}

}
