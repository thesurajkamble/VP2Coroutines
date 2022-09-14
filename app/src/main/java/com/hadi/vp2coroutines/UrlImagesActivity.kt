package com.hadi.vp2coroutines

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.palette.graphics.Palette
import androidx.viewpager2.widget.ViewPager2
import coil.ImageLoader
import coil.bitmap.BitmapPool
import coil.load
import coil.request.ImageRequest
import coil.request.SuccessResult
import coil.size.Scale
import coil.size.Size
import coil.transform.BlurTransformation
import coil.transform.Transformation
import com.hadi.vp2coroutines.data.dota2HeroesName
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

    private fun generateNames() {
        val dotaNameParser = dota2HeroesName.map {
            val nameParse = it.replace(" ", "_").toLowerCase(Locale.getDefault())


            val urlImage =
                "https://cdn.cloudflare.steamstatic.com/apps/dota2/videos/dota_react/heroes/renders/${nameParse}.png"
//            Log.e("Data", urlImage)

            urlImage
        }.toMutableList()
        imagesList.addAll(dotaNameParser)

//        generateNewListWithColors()
    }

    private fun setupData() {
        //GET LIST WITH COLORS
        Log.d("Main", "Antes")
        generateNames()
//        imageListGenerate = getListWithColors().toMutableList()
//        generateNewListWithColors()

        Log.d("Main", "Despues")
        Log.d("UrlImagesActivity", "imageListGenerate $imageListGenerate")
//        https://www.johanneskueber.com/posts/android_coil_palette/
//https://www.dota2.com/heroes
        //https://github.com/Den-dp/dota2-heroes/blob/master/src/dota2-heroes.json
//        imagesList.add("gggg")
//        imagesList.add("https://cdn.pixabay.com/photo/2020/12/10/09/22/beach-front-5819728_960_720.jpg")
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
        //Log.d("UrlImagesActivity_Map", imageData)
        (imageData.toGenerateColorsURLImage())
//        (imageData.getColorsFromUrlImage())


    }

    fun URL.toBitmap(): Bitmap? {
        return try {
            BitmapFactory.decodeStream(openStream())
        } catch (e: IOException) {
            null
        }
    }

    private fun generateNewListWithColors() {
        Log.d(TAG, "M=>$imagesList")
        imagesList.map { imageData ->
//            Log.d(TAG, "each $imageData")
            CoroutineScope(Job() + Dispatchers.IO).launch {
                try {
                    //-13571856
                    val loader = ImageLoader(context = baseContext)
                    val req = ImageRequest.Builder(baseContext)
                        .data(imageData)
                        .transformations(object : Transformation {
                            override fun key() = "paletteTransformer"
                            override suspend fun transform(
                                pool: BitmapPool,
                                input: Bitmap,
                                size: Size,
                            ): Bitmap {
                                val palette = Palette.from(input).generate()

                                val swatch = palette.vibrantSwatch
                                Log.d(TAG, "RemoteAdapter" + palette.vibrantSwatch?.rgb.toString())
                                if (swatch != null) {
//                            itemView.setBackgroundColor(palette.vibrantSwatch?.rgb ?: ContextCompat.getColor(
//                                itemView.context,
//                                R.color.purple_200
//                            ))
                                }
                                return input
                            }


                        }).crossfade(true)


                        .scale(Scale.FIT)
                        .build()


//                    var image: Bitmap? = null
//                    val url = URL(imageData)
//                    val bitMap = url.toBitmap()
//                    if (bitMap != null) {
//                        image = Bitmap.createScaledBitmap(bitMap, 100, 100, true)
//                        //D/RemoteAdapter: -13571856
//
//                        val palette = Palette.from(image).generate()
//
//                        val swatch = palette.vibrantSwatch
//                        Log.d(TAG, "PALETTE" + palette.vibrantSwatch?.rgb.toString())
//                        val colorGenerate = (palette.vibrantSwatch?.rgb ?: ContextCompat.getColor(
//                            baseContext,
//                            R.color.purple_200
//                        ))
//                        val heroNameImageColor = ImageUrlColorsGenerate(
//                            imageUrl = imageData,
//                            colorGenerate = colorGenerate
//
//                        )
//                        Log.d(TAG, "O=>$heroNameImageColor")
//                        imageListGenerate.add(
//                            heroNameImageColor
//                        )
//                    }

                } catch (e: IOException) {
//                // Log exception
                    Log.e("Error: ", e.message.toString())
                }
            }

        }

//        }
    }

    data class ImageUrlColorsGenerate(val imageUrl: String, val colorGenerate: Int)

    // Generate palette synchronously and return it
    private fun createPaletteSync(bitmap: Bitmap): Palette = Palette.from(bitmap).generate()

    fun createPaletteAsync(bitmap: Bitmap) {
        Palette.from(bitmap).generate { palette ->

            if (palette != null) {
                Log.d("createPaletteAsync", palette.vibrantSwatch?.rgb.toString())
            }

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

    private suspend fun uriToBitmap(context: Context, uri: Uri?): Bitmap {

        val loader = ImageLoader(context)
        val request = ImageRequest.Builder(context)
            .data(uri)
            .allowHardware(false) // Disable hardware bitmaps.
            .build()

        val result = (loader.execute(request) as SuccessResult).drawable
        val bitmap = (result as BitmapDrawable).bitmap

        val resizedBitmap = Bitmap.createScaledBitmap(
            bitmap, 80, 80, true);

        return resizedBitmap
    }

    private fun String.getColorsFromUrlImage(): ImageUrlColorsGenerate {
        CoroutineScope(Dispatchers.IO).launch {


            val bitmap = uriToBitmap(
                context = baseContext, uri = Uri.parse(this.toString())
            )
//            val color = createPaletteSync(bitmap).vibrantSwatch
            val palette = createPaletteSync(bitmap)

            Log.d("UrlImagesActivity", palette.vibrantSwatch?.rgb.toString())


            binding.containerConstraint.setBackgroundColor(palette.vibrantSwatch?.rgb
                ?: ContextCompat.getColor(
                    baseContext,
                    R.color.purple_200
                ))

        }
        return ImageUrlColorsGenerate(
            imageUrl = this,
            colorGenerate = 2

        )
    }

    private fun String.toGenerateColorsURLImage(): ImageUrlColorsGenerate {
        var colorGenerate = 0


//        CoroutineScope(Job() + Dispatchers.IO).launch {
//            try {
//                val loader = ImageLoader(context = baseContext)
//                val req = ImageRequest.Builder(baseContext)
//                    .data(this)
//                    .target { result ->
//                        val bitmap = (result as BitmapDrawable).bitmap
//                        val color = createPaletteSync(bitmap).vibrantSwatch
//
//                        Log.d("UrlImagesActivity Color", color?.rgb.toString())
//                    }
//                    .build()
//                val disposable = loader.enqueue(req)
//            } catch (e: IOException) {
////                // Log exception
//                Log.e("Error: ",e.localizedMessage)
//            }
//
//
//        }

        CoroutineScope(Job() + Dispatchers.IO).launch {
            try {

                //-13571856
                val loader = ImageLoader(context = baseContext)
                val req = ImageRequest.Builder(baseContext)
                    .data(this)
                    .target { result ->
                        val bitmap = (result as BitmapDrawable).bitmap
                        val color = createPaletteSync(bitmap).vibrantSwatch

                        Log.d("UrlImagesActivity Color", color?.rgb.toString())
                    }
                    .build()
                val disposable = loader.enqueue(req)

//                var image: Bitmap? = null
//
//                val url = URL(this@toGenerateColorsURLImage)
//                val bitMap = BitmapFactory.decodeStream(url.openConnection().getInputStream())
//                image = Bitmap.createScaledBitmap(bitMap, 100, 100, true)
//                //D/RemoteAdapter: -13571856
////                val color = createPaletteSync(bitMap).vibrantSwatch
//                val palette = Palette.from(image).generate()
//
//                val swatch = palette.vibrantSwatch
//                Log.d("UrlImagesActivity Color", palette.vibrantSwatch?.rgb.toString())
//
//                val colorGenerate = (palette.vibrantSwatch?.rgb ?: ContextCompat.getColor(
//                    baseContext,
//                    R.color.purple_200
//                ))
//                imageListGenerate.add(
//                    ImageUrlColorsGenerate(
//                        imageUrl = this@toGenerateColorsURLImage,
//                        colorGenerate = colorGenerate
//
//                    )
//                )

            } catch (e: IOException) {
                // Log exception
            }
        }

        return ImageUrlColorsGenerate(
            imageUrl = this,
            colorGenerate = colorGenerate

        )
    }

    private suspend fun getBitmap(context: Context, url: String): Bitmap? {
        var bitmap: Bitmap? = null
        val request = ImageRequest.Builder(context)
            .data(url)
            .transformations(object : Transformation {
                override fun key() = "paletteTransformer"
                override suspend fun transform(
                    pool: BitmapPool,
                    input: Bitmap,
                    size: Size,
                ): Bitmap {
                    val palette = Palette.from(input).generate()

                    val swatch = palette.vibrantSwatch
                    Log.d(TAG, "paleta " + palette.vibrantSwatch?.rgb.toString())
                    if (swatch != null) {
//                            itemView.setBackgroundColor(palette.vibrantSwatch?.rgb ?: ContextCompat.getColor(
//                                itemView.context,
//                                R.color.purple_200
//                            ))
                    }
                    return input
                }


            }).crossfade(true)
            .scale(Scale.FIT)
            .build()


        return bitmap
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
//                    val currentImage = imagesList[position]
//                    binding.imageContainerBlur.load(currentImage) {
//
//                        transformations(BlurTransformation(context, radius = 24f, sampling = 2f))
//
//
//                    }
                    Log.d(TAG, "onPageScrolled")
                }

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)


                    val currentImage = imagesList[position]
//                    Log.d("UrlImagesActivity", "imageListGenerate $imageListGenerate")



                    if (imageListGenerate.isNotEmpty()) {
                        val colorGenerate = imageListGenerate[position].colorGenerate
                        binding.containerConstraint.setBackgroundColor(colorGenerate)
                    }


                    binding.imageContainerBlur.load(currentImage) {

//                        transformations(BlurTransformation(context, radius = 24f, sampling = 2f))

                        transformations(BlurTransformation(context, radius = 24f, sampling = 2f),
                            object : Transformation {
                                override fun key() = "paletteTransformer"
                                override suspend fun transform(
                                    pool: BitmapPool,
                                    input: Bitmap,
                                    size: Size,
                                ): Bitmap {
                                    val palette = Palette.from(input).generate()

                                    val swatch = palette.vibrantSwatch
                                    Log.d(TAG,"UrlImage"+ palette.vibrantSwatch?.rgb.toString())
//                                    if (swatch != null) {
//                                        binding.containerConstraint.setBackgroundColor(palette.vibrantSwatch?.rgb
//                                            ?: ContextCompat.getColor(
//                                                context,
//                                                R.color.purple_200
//                                            ))
//                                    }
                                    return input
                                }


                            }
                        )
                        crossfade(true)


                        scale(Scale.FIT)
                        build()
                    }
                }
            })

        }


    }

    companion object {
        const val TAG = "UrlImagesActivity"
    }
}