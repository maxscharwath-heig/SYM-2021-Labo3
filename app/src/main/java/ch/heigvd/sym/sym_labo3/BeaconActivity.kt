package ch.heigvd.sym.sym_labo3

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.altbeacon.beacon.*

const val BEACON_PARSER = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"
const val BEACON_REGION = "all-beacons-region"

/**
 * Activity for the beacon part of the application
 */
class BeaconActivity : AppCompatActivity() {

    //array for the beacons that will be found. This list will be used for the adapter.
    private val beaconList = ArrayList<Beacon>()

    private lateinit var beaconArrayAdapter: BeaconAdapter

    // Ask for location access permission
    private val beaconPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)
            if (!isGranted) {
                closeOnNotGrantedLocation()
            }
        }

    // Behaviour if location access is not granted
    private fun closeOnNotGrantedLocation() {
        Toast.makeText(this, R.string.must_access_location, Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beacon)

        beaconPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)

        //Here we set the beaconManager to be able to detect the beacons we're interested in
        val beaconsView = findViewById<RecyclerView>(R.id.beacons)
        val beaconParser = BeaconParser().setBeaconLayout(BEACON_PARSER)
        val beaconManager = BeaconManager.getInstanceForApplication(this)
        beaconManager.beaconParsers.clear()
        beaconManager.beaconParsers.add(beaconParser)
        val region = Region(BEACON_REGION, null, null, null)
        beaconManager.getRegionViewModel(region).rangedBeacons.observe(this, rangingObserver)

        //Here we start the actual process of detecting the beacons
        beaconManager.startRangingBeacons(region)

        //Creation and configuration of the adapter for the recyclerview
        beaconArrayAdapter = BeaconAdapter(beaconList, applicationContext)
        beaconsView.layoutManager = LinearLayoutManager(this)
        beaconsView.itemAnimator = DefaultItemAnimator()
        beaconsView.adapter = beaconArrayAdapter
        beaconsView.addItemDecoration(
            DividerItemDecoration(
                applicationContext,
                LinearLayoutManager.VERTICAL
            )
        )
    }

    /**
     * Observer for the detection of the beacons
     */
    private val rangingObserver = Observer<Collection<Beacon>> { beacons ->
        for (beacon: Beacon in beacons) {
            //If we already have the beacon we update it
            if (beaconList.contains(beacon)) {
                beaconList[beaconList.indexOf(beacon)] = beacon
                //We notify the adapter of the change
                beaconArrayAdapter.notifyItemChanged(beaconList.indexOf(beacon))
            //If not we add it to the list
            } else {
                beaconList.add(beacon)
                //We notify the adapter of the insertion
                beaconArrayAdapter.notifyItemInserted(beaconList.indexOf(beacon))
            }
        }
    }
}