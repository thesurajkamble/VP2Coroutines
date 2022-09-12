package com.hadi.vp2coroutines

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.palette.graphics.Palette
import androidx.viewpager2.widget.ViewPager2
import coil.load
import coil.transform.BlurTransformation
import com.hadi.vp2coroutines.data.DotaHeroes
import com.hadi.vp2coroutines.data.DotaHeroesName
import com.hadi.vp2coroutines.data.dota2HeroesName
import com.hadi.vp2coroutines.data.getHeroRole
import com.hadi.vp2coroutines.databinding.ActivityUrlImagesBinding
import com.hadi.vp2coroutines.remoteimages.RemoteSliderAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.URL
import java.util.*

class UrlImagesActivity : AppCompatActivity(R.layout.activity_url_images) {

    private lateinit var binding: ActivityUrlImagesBinding
    private lateinit var sliderAdapter: RemoteSliderAdapter
    private lateinit var itemDecoration: HorizontalMarginItemDecoration
    private val INTERVAL_TIME = 5000L

    private var imagesList = mutableListOf<String>()
    private var imageListGenerate = mutableListOf<ImageUrlColorsGenerate>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_url_images)
        binding = ActivityUrlImagesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sliderAdapter = RemoteSliderAdapter(this@UrlImagesActivity)
        itemDecoration = HorizontalMarginItemDecoration(
            this,
            R.dimen.item_decoration
        )

        setupCarouselSlider()

        setupData()
        setupAdapter()

    }
    private fun generateNames(){
        val dotaNameParser= dota2HeroesName.map {
            val nameParse= it.replace(" ","_").toLowerCase(Locale.getDefault())


            val urlImage= "https://cdn.cloudflare.steamstatic.com/apps/dota2/videos/dota_react/heroes/renders/${nameParse}.png"
            Log.e("Data",urlImage)
            urlImage
        }.toMutableList()
        imagesList.addAll(dotaNameParser)
    }
    private fun setupData() {
        //GET LIST WITH COLORS
        imageListGenerate = getListWithColors().toMutableList()
//        https://www.johanneskueber.com/posts/android_coil_palette/
//https://www.dota2.com/heroes
        //https://github.com/Den-dp/dota2-heroes/blob/master/src/dota2-heroes.json
//        imagesList.add("gggg")
//        imagesList.add("https://cdn.pixabay.com/photo/2020/12/10/09/22/beach-front-5819728_960_720.jpg")
        generateNames()
//        imagesList.add("https://cdn.cloudflare.steamstatic.com/apps/dota2/videos/dota_react/heroes/renders/lina.png")
//        imagesList.add("https://cdn.cloudflare.steamstatic.com/apps/dota2/videos/dota_react/heroes/renders/bloodseeker.png")
//        imagesList.add("https://www.pngplay.com/wp-content/uploads/11/Dota-2-PNG-HD-Free-File-Download.png")
//        imagesList.add("https://cdn.cloudflare.steamstatic.com/apps/dota2/videos/dota_react/heroes/renders/slark.png")
//        imagesList.add("https://cdn.cloudflare.steamstatic.com/apps/dota2/videos/dota_react/heroes/renders/zuus.png")
//        imagesList.add("https://cdn.cloudflare.steamstatic.com/apps/dota2/videos/dota_react/heroes/renders/lion.png")


//        imagesList.add("https://cdn.pixabay.com/photo/2021/03/04/15/29/river-6068374_960_720.jpg")
//        imagesList.add("https://cdn.pixabay.com/photo/2021/03/29/08/22/peach-flower-6133330_960_720.jpg")
    }

    private fun getListWithColors() = imagesList.map { imageData ->
        (imageData.toGenerateColorsURLImage())

    }

    data class ImageUrlColorsGenerate(val imageUrl: String, val colorGenerate: Int)

    // Generate palette synchronously and return it
    private fun createPaletteSync(bitmap: Bitmap): Palette = Palette.from(bitmap).generate()

    fun createPaletteAsync(bitmap: Bitmap) {
        Palette.from(bitmap).generate { palette ->
            binding.containerConstraint.apply {
                setBackgroundColor(
                    (palette?.vibrantSwatch?.rgb ?: ContextCompat.getColor(
                        baseContext,
                        R.color.purple_200
                    ))
                )
            }


        }
    }


    private fun String.toGenerateColorsURLImage(): ImageUrlColorsGenerate {
        var colorGenerate = 0
        var image: Bitmap? = null
        CoroutineScope(Job() + Dispatchers.IO).launch {
            try {
                val url = URL(this@toGenerateColorsURLImage)
                val bitMap = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                image = Bitmap.createScaledBitmap(bitMap, 100, 100, true)

                val color = createPaletteSync(bitMap).vibrantSwatch
                val colorGenerate = (color?.rgb ?: ContextCompat.getColor(
                    baseContext,
                    R.color.purple_200
                ))

                ImageUrlColorsGenerate(
                    imageUrl = this@toGenerateColorsURLImage,
                    colorGenerate = colorGenerate

                )

            } catch (e: IOException) {
                // Log exception
            }
        }

        return ImageUrlColorsGenerate(
            imageUrl = this,
            colorGenerate = colorGenerate

        )
    }

    private fun setupAdapter() {
        sliderAdapter.setImages(imagesList)
        sliderAdapter.notifyDataSetChanged()
    }

    //region REGION FULL SCREEN WAY
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideSystemUIAndNavigation(this)
            adjustToolbarMarginForNotch()
        }
    }

    private fun hideSystemUIAndNavigation(activity: Activity) {
        val decorView: View = activity.window.decorView
        decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_IMMERSIVE
                    // Set the content to appear under the system bars so that the
                    // content doesn't resize when the system bars hide and show.
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN // Hide the nav bar and status bar
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    @SuppressLint("NewApi")
    private fun adjustToolbarMarginForNotch() {
        // Notch is only supported by >= Android 9
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val windowInsets = window.decorView.rootWindowInsets
            if (windowInsets != null) {
                val displayCutout = windowInsets.displayCutout
                if (displayCutout != null) {
                    val safeInsetTop = displayCutout.safeInsetTop
//                    val newLayoutParams = binding.toolbar.layoutParams as ViewGroup.MarginLayoutParams
//                    newLayoutParams.setMargins(0, safeInsetTop, 0, 0)
//                    binding.toolbar.layoutParams = newLayoutParams
                }
            }
        }
    }
    //endregion

    private fun setupCarouselSlider() {
        binding.viewpager.apply {
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            adapter = sliderAdapter
            setCarouselEffects()
            addItemDecoration(itemDecoration)
            autoScroll(lifecycleScope, INTERVAL_TIME)
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int,
                ) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                    //                countTxtView.setText(String.format(Locale.ENGLISH,"%d/%d", position+1, matchCourseList.size()));

                }

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)


                    val currentImage = imagesList[position]


                    //region COLOR GENER M 2
//                    createPaletteAsync(
//                        (ContextCompat.getDrawable(
//                            context,
//                            currentImage
//                        ) as BitmapDrawable).bitmap
//                    )
                    //endregion


//                    val colorGenerate = imageListGenerate[position].colorGenerate
//
//                    binding.containerConstraint.setBackgroundColor(colorGenerate)

                    binding.imageContainerBlur.load(currentImage) {

                        transformations(BlurTransformation(context, radius = 24f, sampling = 2f))
                    }
                }
            })

        }


    }
}