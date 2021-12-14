package ch.heigvd.sym.sym_labo3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.altbeacon.beacon.*

const val BEACON_PARSER = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"
const val BEACON_REGION = "all-beacons-region"

class BeaconActivity : AppCompatActivity() {

    private val beaconList = ArrayList<Beacon>()
    private lateinit var beaconArrayAdapter: BeaconAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        beaconArrayAdapter = BeaconAdapter(beaconList, applicationContext)
        setContentView(R.layout.activity_beacon)


        val beaconsView = findViewById<RecyclerView>(R.id.beacons)
        val beaconParser = BeaconParser().setBeaconLayout(BEACON_PARSER)
        val beaconManager = BeaconManager.getInstanceForApplication(this)
        beaconManager.beaconParsers.clear()
        beaconManager.beaconParsers.add(beaconParser)
        val region = Region(BEACON_REGION, null, null, null)
        beaconManager.getRegionViewModel(region).rangedBeacons.observe(this, rangingObserver)
        beaconManager.startRangingBeacons(region)

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

    private val rangingObserver = Observer<Collection<Beacon>> { beacons ->
        for (beacon: Beacon in beacons) {
            if (beaconList.contains(beacon)) {
                beaconList[beaconList.indexOf(beacon)] = beacon
                beaconArrayAdapter.notifyItemChanged(beaconList.indexOf(beacon))
            } else {
                beaconList.add(beacon)
                beaconArrayAdapter.notifyItemInserted(beaconList.indexOf(beacon))
            }
        }
    }
}