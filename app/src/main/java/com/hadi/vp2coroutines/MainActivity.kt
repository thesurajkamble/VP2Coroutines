package com.hadi.vp2coroutines

import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import coil.load
import coil.transform.BlurTransformation
import com.hadi.vp2coroutines.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val INTERVAL_TIME = 5000L
    private var imagesList = mutableListOf<String>()
    private lateinit var sliderAdapter: SliderAdapter
    private lateinit var itemDecoration: HorizontalMarginItemDecoration

    val imageDataList = listOf(
        ImageData(1, R.drawable.clash_of_clans_archer),
        ImageData(2, R.drawable.pngwing_3),
        ImageData(3, R.drawable.pngwing_1),
        ImageData(4, R.drawable.pngwing_4),
        ImageData(5, R.drawable.pngwing_5),
        ImageData(6, R.drawable.pngwing_6),
        ImageData(7, R.drawable.pngwing_7),
        ImageData(8, R.drawable.pngwing_8),
        ImageData(9, R.drawable.pngwing_9),
        ImageData(10, R.drawable.pngwing_10),
        ImageData(11, R.drawable.pngwing_11),
        ImageData(12, R.drawable.pngwing_12),
        ImageData(13, R.drawable.pngwing_13),
        ImageData(14, R.drawable.pngwing_14)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sliderAdapter = SliderAdapter(this@MainActivity)
        itemDecoration = HorizontalMarginItemDecoration(
            this,
            R.dimen.item_decoration
        )


        setupCarouselSlider()

//        setupNormalSlider()
        setupData()
        setupAdapter()
        //binding.containerConstraint.setBackgroundResource(R.drawable.clash_of_clans_archer)

    }

//    private fun setupNormalSlider() {
//        binding.viewpagerNormal.apply {
//            orientation = ViewPager2.ORIENTATION_HORIZONTAL
//            adapter = sliderAdapter
//            addItemDecoration(itemDecoration)
//            autoScroll(lifecycleScope, INTERVAL_TIME)
//        }
//    }

    private fun setupCarouselSlider() {
        binding.viewpager.apply {
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            adapter = sliderAdapter
            setCarouselEffects()
            addItemDecoration(itemDecoration)
            autoScroll(lifecycleScope, INTERVAL_TIME)
            registerOnPageChangeCallback(object : OnPageChangeCallback() {
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

                    val color = (Math.random() * 16777215).toInt() or (0xFF shl 24)
                    binding.containerConstraint.setBackgroundColor(color)

//                    val currentImage = imagesList.get(position)
                    val currentImage = imagesList.get(position).toInt()

//                    binding.containerConstraint.setBackgroundResource(currentImage)


//                    val imageSelected = BitmapFactory.decodeResource(
//                        resources,
//                        currentImage
//                    )

                    binding.imageContainerBlur.load(currentImage) {

                        transformations(BlurTransformation(context, radius = 24f, sampling = 2f))
                    }
                }
            })

        }

//        binding.viewpager.registerOnPageChangeCallback(
//            object : OnPageChangeCallback() {
//                override fun onPageScrolled(
//                    position: Int,
//                    positionOffset: Float,
//                    positionOffsetPixels: Int,
//                ) {
//                    super.onPageScrolled(position, positionOffset, positionOffsetPixels)
//                    //                countTxtView.setText(String.format(Locale.ENGLISH,"%d/%d", position+1, matchCourseList.size()));
//
//                }
//
//                override fun onPageSelected(position: Int) {
//                    super.onPageSelected(position)
//
//                    val color = (Math.random() * 16777215).toInt() or (0xFF shl 24)
//                    binding.containerConstraint.setBackgroundColor(color)
//                }
//            })
    }


    val links = listOf(
        "https://images.freeimages.com/images/large-previews/825/linked-hands-1308777.jpg",
        "https://images.unsplash.com/photo-1541443131876-44b03de101c5?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&dl=mathieu-renier-4WBvCqeMaDE-unsplash.jpg",
        "https://images.unsplash.com/photo-1549399542-7e3f8b79c341?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&dl=roberto-nickson-zu95jkyrGtw-unsplash.jpg",
        "https://www.cnet.com/a/img/XtH050ErlMIQxKn_HYUx2plJnDc=/940x528/2020/12/17/c9a829c8-69d6-4299-b2d0-cf9624aa7556/2021-acura-tlx-a-spec-65.jpg",
        "https://cdn.jdpower.com/JDPA_2021%20Acura%20TLX%20Advance%20Red%20Front%20View.jpg",
        "https://s3-us-east-2.amazonaws.com/matter-blog/2020/09/People_Person_Cover_Image.png",
        "https://images.fandango.com/ImageRenderer/0/0/redesign/static/img/default_poster.png/0/images/masterrepository/other/ant_man_ver5.jpg"
    )

    private fun setupAdapter() {
        sliderAdapter.setImages(imagesList)
        sliderAdapter.notifyDataSetChanged()
    }

    data class ImageData(val id: Int, @DrawableRes val imageResource: Int)

    private fun setupData() {


//        imagesList.addAll(links.map { imageData -> imageData })
        imagesList.addAll(imageDataList.map { imageData -> imageData.imageResource.toString() })

//        imagesList.add("https://cdn.pixabay.com/photo/2020/12/10/09/22/beach-front-5819728_960_720.jpg")
//        imagesList.add("https://cdn.pixabay.com/photo/2020/09/03/13/56/pine-5541335_960_720.jpg")
//        imagesList.add("https://cdn.pixabay.com/photo/2021/03/04/15/29/river-6068374_960_720.jpg")
//        imagesList.add("https://cdn.pixabay.com/photo/2021/03/29/08/22/peach-flower-6133330_960_720.jpg")
    }
}
