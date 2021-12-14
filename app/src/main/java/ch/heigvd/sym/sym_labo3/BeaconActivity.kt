package ch.heigvd.sym.sym_labo3

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.altbeacon.beacon.Beacon
import org.altbeacon.beacon.BeaconManager
import org.altbeacon.beacon.BeaconParser
import org.altbeacon.beacon.Region
import java.time.LocalDateTime


class BeaconActivity : AppCompatActivity() {

    val beaconList = ArrayList<Beacon>()
    val beaconArrayAdapter = BeaconAdapter(beaconList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_beacon)

        val beaconsView = findViewById<RecyclerView>(R.id.beacons)

        val beaconParser =
            BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24")

        val beaconManager = BeaconManager.getInstanceForApplication(this)
        beaconManager.beaconParsers.clear()
        beaconManager.beaconParsers.add(beaconParser)
        val region = Region("all-beacons-region", null, null, null)
        // Set up a Live Data observer so this Activity can get ranging callbacks
        // observer will be called each time the monitored regionState changes (inside vs. outside region)
        beaconManager.getRegionViewModel(region).rangedBeacons.observe(this, rangingObserver)
        beaconManager.startRangingBeacons(region)


        beaconsView.layoutManager = LinearLayoutManager(this)
        beaconsView.itemAnimator = DefaultItemAnimator()
        beaconsView.adapter = beaconArrayAdapter
    }

    val rangingObserver = Observer<Collection<Beacon>> { beacons ->
        Log.d(TAG, "Ranged: ${beacons.count()} beacons")
        for (beacon: Beacon in beacons) {
            Log.d(TAG, "$beacon about ${beacon.distance} meters away")
            if (beaconList.contains(beacon)) {
                beaconList[beaconList.indexOf(beacon)] = beacon
                beaconArrayAdapter.notifyItemChanged(beaconList.indexOf(beacon))
            } else {
                beaconList.add(beacon)
                beaconArrayAdapter.notifyItemInserted(beaconList.indexOf(beacon))
            }
        }
    }

    class BeaconAdapter(private val beaconList: List<Beacon>) :
        RecyclerView.Adapter<BeaconAdapter.ViewHolder>() {

        // create new views
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            // inflates the card_view_design view
            // that is used to hold list item
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.beacon, parent, false)
            return ViewHolder(view)
        }

        // binds the list items to a view
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            val beacon = beaconList[position]
            val time = LocalDateTime.now()
            // sets the text to the textview from our itemHolder class
            holder.idTextView.text = "Id : " + (position + 1)
            holder.uuidTextView.text = "UUID : " + beacon.id1
            holder.majorTextView.text = "major : " + beacon.id2
            holder.minorTextView.text = "minor : " + beacon.id3
            holder.rssiTextView.text = "rssi : " + beacon.rssi
            holder.distanceTextView.text =
                "distance : " + String.format("%.3f", beacon.distance) + " m"
            holder.dateTextView.text = "last detection time : " +
                    time.dayOfMonth + "-" + time.monthValue + "-" + time.year + "  " +
                    time.hour + ":" + time.minute + ":" + time.second
        }

        // return the number of the items in the list
        override fun getItemCount(): Int {
            return beaconList.size
        }

        // Holds the views for adding it to image and text
        class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
            val idTextView: TextView = itemView.findViewById(R.id.id)
            val uuidTextView: TextView = itemView.findViewById(R.id.UUID)
            val majorTextView: TextView = itemView.findViewById(R.id.major)
            val minorTextView: TextView = itemView.findViewById(R.id.minor)
            val rssiTextView: TextView = itemView.findViewById(R.id.rssi)
            val distanceTextView: TextView = itemView.findViewById(R.id.distance)
            val dateTextView: TextView = itemView.findViewById(R.id.date)

        }
    }
}