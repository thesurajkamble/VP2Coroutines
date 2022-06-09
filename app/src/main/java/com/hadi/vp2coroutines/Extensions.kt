package com.hadi.vp2coroutines

import android.view.View
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.viewpager2.widget.ViewPager2
import kotlinx.coroutines.delay
import kotlin.math.abs

suspend fun ViewPager2.scrollIndefinitely(interval: Long) {
    delay(interval)
    val numberOfItems = adapter?.itemCount ?: 0
    val lastIndex = if (numberOfItems > 0) numberOfItems - 1 else 0
    val nextItem = if (currentItem == lastIndex) 0 else currentItem + 1

    setCurrentItem(nextItem, true)

    scrollIndefinitely(interval)
}

fun ViewPager2.autoScroll(lifecycleScope: LifecycleCoroutineScope, interval: Long) {
    lifecycleScope.launchWhenResumed {
        scrollIndefinitely(interval)
    }
}

fun ViewPager2.setCarouselEffects(){
    offscreenPageLimit = 1

    val nextItemVisiblePx = resources.getDimension(R.dimen.viewpager_next_item_visible).toInt()
    val currentItemHorizontalMarginPx = resources.getDimension(R.dimen.viewpager_current_item_horizontal_margin).toInt()
    val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx



    val pageTransformer = ViewPager2.PageTransformer { page: View, position: Float ->
        page.translationX = - pageTranslationX * position
        page.scaleY = 1 - (0.25f * abs(position))
        page.alpha = 0.5f + (1 - abs(position))
    }

    // Add a PageTransformer that translates the next and previous items horizontally
    // towards the center of the screen, which makes them visible
//    val nextItemVisiblePx2 = resources.getDimension(R.dimen.viewpager_next_item_visible).toInt()
//    val currentItemHorizontalMarginPx2 = resources.getDimension(R.dimen.viewpager_current_item_horizontal_margin).toInt()
//    val pageTranslationX2 = nextItemVisiblePx2 + currentItemHorizontalMarginPx2
//    val pageTransformer2 = ViewPager2.PageTransformer { page: View, position: Float ->
//        page.translationX = -pageTranslationX2 * position
//        // Next line scales the item's height. You can remove it if you don't want this effect
//        page.scaleY = 1 - 0.15f * abs(position)
//    }
//    setPageTransformer(pageTransformer2)

    setPageTransformer(pageTransformer)
}
