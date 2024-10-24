package com.drag0n.weatherf0recastn3w.presentation

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.icu.util.Calendar
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.drag0n.weatherf0recastn3w.Const
import com.drag0n.weatherf0recastn3w.Const.APP_GALERY
import com.drag0n.weatherf0recastn3w.Const.PREMIUM_KEY
import com.drag0n.weatherf0recastn3w.Const.RUSTORE
import com.drag0n.weatherf0recastn3w.domane.Room.ItemCity
import com.drag0n.weatherf0recastn3w.DialogManager
import com.drag0n.weatherf0recastn3w.MainViewModel
import com.drag0n.weatherf0recastn3w.adapter.VpAdapter
import com.drag0n.weatherf0recastn3w.databinding.ActivityMainBinding
import com.drag0n.weatherf0recastn3w.R
import com.drag0n.weatherf0recastn3w.domane.Room.CityListDataBase
import com.drag0n.weatherf0recastn3w.adapter.ItemCityAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.huawei.hms.api.HuaweiApiAvailability
import com.huawei.hms.location.LocationAvailability
import com.huawei.hms.location.LocationCallback
import com.huawei.hms.location.LocationRequest
import com.huawei.hms.location.LocationResult
import com.yandex.mobile.ads.banner.BannerAdSize
import com.yandex.mobile.ads.common.AdRequest
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.rustore.sdk.billingclient.RuStoreBillingClient
import ru.rustore.sdk.billingclient.RuStoreBillingClientFactory
import ru.rustore.sdk.billingclient.model.purchase.PaymentResult
import ru.rustore.sdk.billingclient.model.purchase.Purchase
import ru.rustore.sdk.billingclient.model.purchase.PurchaseState
import ru.rustore.sdk.billingclient.usecase.PurchasesUseCase
import ru.rustore.sdk.billingclient.utils.pub.checkPurchasesAvailability
import ru.rustore.sdk.core.feature.model.FeatureAvailabilityResult
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.UUID

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ItemCityAdapter.onClick {
    lateinit var binding: ActivityMainBinding
    private lateinit var vpAdapter: VpAdapter
    private lateinit var pLauncher: ActivityResultLauncher<String>
    private lateinit var fLocotionClient: FusedLocationProviderClient
    private lateinit var fLocotionClientHMS: com.huawei.hms.location.FusedLocationProviderClient
    private lateinit var adapter: ItemCityAdapter
    private lateinit var db: CityListDataBase

    val model: MainViewModel by viewModels()

    private lateinit var billingClient: RuStoreBillingClient
    private lateinit var purchasesUseCase: PurchasesUseCase
    private lateinit var pref: SharedPreferences
    private lateinit var edit: SharedPreferences.Editor



    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        initSharedPreferense()
        initRustoreBilling()
        initDB()
        initLocation()
        initVP()
        initRcView()
        super.onCreate(savedInstanceState)
        model.premium.value = pref.getBoolean(PREMIUM_KEY, false)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            billingClient.onNewIntent(intent)
        }

        val calendar = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Calendar.getInstance().time)

        model.premium.observe(this){premium->
            if(premium) binding.yaMob.visibility = View.GONE
            else initYaMov()
        }

        model.load.observe(this){
            if (model.load.value == true) binding.progressBar2.visibility = View.VISIBLE
            else binding.progressBar2.visibility = View.GONE
        }

        model.responseAstronomy.observe(this) {
            val code = model.responseCurrent.value?.current?.condition?.code
            val timeSunrise = it.astronomy.astro.sunrise
            val sunrise = SimpleDateFormat("hh:mm aa", Locale.getDefault()).parse(timeSunrise)
                ?.let { sunrise -> SimpleDateFormat("HH:mm", Locale.getDefault()).format(sunrise) }

            val timeSunset = it.astronomy.astro.sunset
            val sunset = SimpleDateFormat("hh:mm aa", Locale.getDefault()).parse(timeSunset)
                ?.let { sunset -> SimpleDateFormat("HH:mm", Locale.getDefault()).format(sunset) }


            if (calendar > sunset!! || calendar < sunrise!!)
                when (code) {
                    1009 -> insertBackground(R.drawable.img_pasm_night) // пасмурно
                    1087, 1273, 1276, 1282 -> insertBackground(R.drawable.img_1_night)
                    // гроза
                    1150, 1153, 1168, 1171 -> insertBackground(R.drawable.img_2_night) // морось
                    1063, 1072, 1180, 1183, 1186, 1189, 1192, 1195, 1198, 1201, 1240, 1243,1246,1249,1252
                    -> insertBackground(R.drawable.img_3_night)
                    // дождь
                    1066, 1069, 1114, 1117, 1204, 1207, 1210, 1213, 1216, 1219, 1222, 1225, 1237, 1255, 1258,1261, 1264
                    -> insertBackground(R.drawable.img_4_night)
                    // снег
                    1030, 1147, 1135 -> insertBackground(R.drawable.img_5_night) // туман
                    1000 -> insertBackground(R.drawable.img_night) // Чистое небо
                    else -> insertBackground(R.drawable.img_8)
                } // Меняет фон вечером
            else {
                when (code) {
                    1009 -> insertBackground(R.drawable.img_pasm) // пасмурно
                    1087, 1273, 1276, 1282 -> insertBackground(R.drawable.img_1_day)
                    // гроза
                    1150, 1153, 1168, 1171 -> insertBackground(R.drawable.img_2_day) // морось
                    1063, 1072, 1180, 1183, 1186, 1189, 1192, 1195, 1198, 1201, 1240, 1243,1246,1249,1252
                    -> insertBackground(R.drawable.img_3_day)
                    // дождь
                    1066, 1069, 1114, 1117, 1204, 1207, 1210, 1213, 1216, 1219, 1222, 1225, 1237, 1255, 1258,1261, 1264
                    -> insertBackground(R.drawable.img_4_day)
                    // снег
                    1030, 1147, 1135 -> insertBackground(R.drawable.img_5_day) // туман
                    1000 -> insertBackground(R.drawable.img) // Чистое небо
                    else -> insertBackground(R.drawable.img_6)
                } // Меняет фон днем
            }
        }

        with(binding) {
            imMenu.setOnClickListener { binding.drawer.openDrawer(GravityCompat.START) }
            bMyCity.setOnClickListener {
                model.load.value = true
                chekLocation()
                binding.drawer.closeDrawer(GravityCompat.START)

            }
            bCallback.setOnClickListener {
                try {
                    binding.drawer.closeDrawer(GravityCompat.START)
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("mailto:apereverzev47@gmail.com")
                        )
                    )
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, getString(R.string.main_error), Toast.LENGTH_SHORT).show()
                }
            }
            bUpdate.setOnClickListener {
                try {
                    binding.drawer.closeDrawer(GravityCompat.START)
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(RUSTORE)

                        )
                    )
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, getString(R.string.main_error), Toast.LENGTH_SHORT).show()
                }
            }
            imBAddMenu.setOnClickListener {
                DialogManager.nameSitySearchDialog(
                    this@MainActivity,
                    object : DialogManager.Listener {
                        override fun onClick(city: String?) {
                            if (city!!.isNotEmpty()) {
                                CoroutineScope(Dispatchers.IO).launch {
                                    db.CourseDao().insertAll(ItemCity(null, city))
                                }
                            } else Toast.makeText(
                                this@MainActivity,
                                getString(R.string.main_isNotEmpty),
                                Toast.LENGTH_SHORT
                            ).show()

                        }

                    })
            }
            bPremium.setOnClickListener {
                proverkaVozmoznoyOplaty(this@MainActivity)
                drawer.closeDrawer(GravityCompat.START)
            }
        }


    } // OnCreate
    override fun onResume() {
        super.onResume()
        chekPermissionLocation()
        model.trueLoad()

        CoroutineScope(Dispatchers.IO).launch {
            shopingList()
        }

    }
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        billingClient.onNewIntent(intent)
    }
    private fun chekPermissionLocation() {
        if (Const.isPermissionGranted(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            chekLocation()
        } else {
            pLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    } // проверяет есть ли разрешение геолокации
    fun chekLocation() {
        if (isLocationEnabled()) {
            if (isHuaweiMobileServicesAvailable(this)) getLastLocationHuawey()
            else getLocationGoogle()
        } else {
            DialogManager.locationSettingsDialog(this, object : DialogManager.Listener {
                override fun onClick(city: String?) {
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }

            })
        }
    } // Функция проверяет включено ли GPS
    private fun isLocationEnabled(): Boolean {
        val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
    } // Функция узнает включено ли GPS
    private fun isHuaweiMobileServicesAvailable(context: Context): Boolean {
        val huaweiApiAvailability = HuaweiApiAvailability.getInstance()
        val resultCode = huaweiApiAvailability.isHuaweiMobileServicesAvailable(context)
        return resultCode == com.huawei.hms.api.ConnectionResult.SUCCESS
    }
    private fun getLastLocationHuawey() {
        fLocotionClientHMS.lastLocation.addOnSuccessListener {
            try {
                val lat = it.latitude.toString()
                val lon = it.longitude.toString()
                model.getCurrent("$lat,$lon")
            } catch (e: NullPointerException) {
                getLocationHuawey()
            }
        }

    } // Функция для получения последней геолокации Хуавея
    private fun getLocationHuawey() {
        val locationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
            .setNumUpdates(1)

        val callback = object : LocationCallback() {
            override fun onLocationAvailability(p0: LocationAvailability?) {
                model.load.value = false
                super.onLocationAvailability(p0)
                Toast.makeText(
                    this@MainActivity,
                    getString(R.string.main_error_geo),
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onLocationResult(it: LocationResult) {
                val lat = it.lastLocation.latitude.toString()
                val lon = it.lastLocation.longitude.toString()
                model.getCurrent("$lat,$lon")
            }
        }
        fLocotionClientHMS.requestLocationUpdates(locationRequest, callback, null)
    } // Функция для получения геолокации Хуавея
    private fun getLocationGoogle() {
        val ct = CancellationTokenSource()
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fLocotionClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, ct.token)
            .addOnCompleteListener {
                try {
                    val lat = it.result.latitude.toString()
                    val lon = it.result.longitude.toString()
                    model.getCurrent("$lat,$lon")
                } catch (_: Exception) {
                    Toast.makeText(
                        this,
                        getString(R.string.main_error_geo),
                        Toast.LENGTH_LONG
                    ).show()
                }

            }


    } // Функция для получения геолокации Гугла
    private fun insertBackground(backgroud: Int) {
        with(binding) {
            root.setBackgroundResource(backgroud)
        }

    }
    override fun onClick(itemCity: ItemCity, action: String) {
        when (action) {
            Const.SEARCH_CITY -> {
                model.trueLoad()
                model.getCurrent(itemCity.name)
                binding.drawer.closeDrawer(GravityCompat.START)
            }

            Const.DELETE_CITY -> CoroutineScope(Dispatchers.IO).launch {
                db.CourseDao().delete(itemCity)
            }
        }

    }
    private fun proverkaVozmoznoyOplaty(context: Context) {
        RuStoreBillingClient.checkPurchasesAvailability(context)
            .addOnSuccessListener { result ->
                when (result) {
                    FeatureAvailabilityResult.Available -> {
                        pokupka()
                    }
                    is FeatureAvailabilityResult.Unavailable -> {
                        Toast.makeText(context, "Оплата временно недоступна", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }.addOnFailureListener {

            }
    } // Проверка возможности оплатить
    private fun pokupka() {
        purchasesUseCase.purchaseProduct(
            productId = "premium_version_weather_forecast",
            orderId = UUID.randomUUID().toString(),
            quantity = 1,
            developerPayload = null,
        ).addOnSuccessListener { paymentResult: PaymentResult ->
            when (paymentResult) {
                is PaymentResult.Success -> {
                    edit.putBoolean(PREMIUM_KEY, true)
                    edit.apply()
                    model.premium.value = true
                    Toast.makeText(
                        this,
                        "Поздравляю! Теперь у вас не будет рекламы",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {
                    Toast.makeText(
                        this,
                        "Произошла ошибка оплаты",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }.addOnFailureListener {
        }
    } // Покупка товара
    private fun shopingList() {
        purchasesUseCase.getPurchases()
            .addOnSuccessListener { purchases: List<Purchase> ->
                if (purchases.isEmpty() && (model.premium.value == true)) {
                    edit.putBoolean(PREMIUM_KEY, false)
                    edit.apply()
                    model.premium.postValue(false)
                }
                purchases.forEach {

                    if (it.productId == "premium_version_weather_forecast" &&
                        (it.purchaseState == PurchaseState.PAID ||
                                it.purchaseState == PurchaseState.CONFIRMED) &&
                        (model.premium.value == false))
                     {
                        edit.putBoolean(PREMIUM_KEY, true)
                        edit.apply()
                         model.premium.postValue(true)
                    }
                }
            }
            .addOnFailureListener{
            }

    } // Запрос ранее совершенных покупок
    private fun initRustoreBilling(){
        billingClient = RuStoreBillingClientFactory.create(
            context = this,
            consoleApplicationId = "2063502125",
            deeplinkScheme = "yourappscheme"
        )
        purchasesUseCase = billingClient.purchases
    }
    private fun initSharedPreferense(){
        pref = this.getSharedPreferences("PREMIUM", Context.MODE_PRIVATE)
        edit = pref.edit()
    }
    private fun initDB(){
        db = Room.databaseBuilder(
            this,
            CityListDataBase::class.java, "CityList"
        ).build()
    }
    private fun initVP(){
        vpAdapter = VpAdapter(this)
        binding.placeHolder.adapter = vpAdapter
        TabLayoutMediator(binding.tabLayout, binding.placeHolder) { tab, pos ->
            tab.text = resources.getStringArray(R.array.vp_title_main)[pos]
        }.attach()
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.placeHolder.isUserInputEnabled = tab.position != 2
                if(tab.position == 2) binding.yaMob.visibility = View.GONE
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }
    private fun initRcView(){
        val rcView = binding.rcView
        adapter = ItemCityAdapter(this)
        rcView.layoutManager = LinearLayoutManager(this)
        rcView.adapter = adapter
        db.CourseDao().getAll().asLiveData().observe(this) {
            adapter.submitList(it)
        }
    }
    private fun initYaMov(){
        binding.yaMob.setAdUnitId(Const.baner)
        binding.yaMob.setAdSize(BannerAdSize.stickySize(this, 350))
        val adRequest = AdRequest.Builder().build()
        if (!pref.getBoolean(PREMIUM_KEY, false))  binding.yaMob.loadAd(adRequest)
    }
    private fun initLocation(){
        pLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {}
        fLocotionClient = LocationServices.getFusedLocationProviderClient(this)
        fLocotionClientHMS =
            com.huawei.hms.location.LocationServices.getFusedLocationProviderClient(this)
    }
}



