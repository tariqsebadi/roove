/*
 * Created by Andrii Kovalchuk
 * Copyright (c) 2020. All rights reserved.
 * Last modified 03.04.20 18:52
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.mmdev.roove.ui.common.custom.components

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout

/**
 * This is the documentation block about the class
 */

class TouchLayout(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

	override fun onTouchEvent(event: MotionEvent?): Boolean {
		return (parent as View).onTouchEvent(event)
	}

}
