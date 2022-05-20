package com.example.pedimearequito.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pedimearequito.R
import com.example.pedimearequito.activities.comercio.orders.detail.ComercioOrdersDetailActivity
import com.example.pedimearequito.models.Order

class OrdersComercioAdapter (val context: Activity, val orders: ArrayList <Order>):RecyclerView.Adapter<OrdersComercioAdapter.OrdersViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_orders_comercio,parent,false)
        return OrdersViewHolder(view)
    }

    override fun getItemCount(): Int {
        return orders.size
    }

    override fun onBindViewHolder(holder: OrdersViewHolder, position: Int) {

        val order = orders[position] //DEVUELVE CADA ORDEN QUE TENEMOS


        holder.textViewOrderId.text = "ORDEN #${order.id}"
        holder.textViewDate.text = "${order.timestamp}"
        holder.textViewAddress.text = "${order.address?.address}"
        holder.textViewClient.text = "${order?.client?.name} ${order?.client?.lastname} "

        holder.itemView.setOnClickListener {goToOrderDetail(order)

        }


    }
    private fun goToOrderDetail(order: Order) {
        val i = Intent(context,ComercioOrdersDetailActivity::class.java)
        i.putExtra("order",order.toJson())
        context.startActivity(i)
    }

    class OrdersViewHolder (view: View): RecyclerView.ViewHolder(view) {

        val textViewOrderId: TextView
        val textViewDate: TextView
        val textViewAddress: TextView
        val textViewClient: TextView

        init {
            textViewOrderId = view.findViewById(R.id.textview_order_id)
            textViewDate = view.findViewById(R.id.textview_date)
            textViewAddress= view.findViewById(R.id.textview_ordenaddress)
            textViewClient= view.findViewById(R.id.textview_order_name_client)

        }

    }
}