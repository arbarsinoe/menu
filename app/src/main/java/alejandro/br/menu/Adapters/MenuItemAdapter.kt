package alejandro.br.menu.Adapters

import alejandro.br.menu.Models.MenuViewModel
import alejandro.br.menu.Models.Pokos.MenuItem
import alejandro.br.menu.Models.Pokos.PedidoItem
import alejandro.br.menu.R
import alejandro.br.menu.activities.DetailMealTwoActivity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class MenuItemAdapter (private val menu : List<MenuItem>, val addBtnClick : View.OnClickListener, val menuViewModel: MenuViewModel) : RecyclerView.Adapter<MenuItemAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val viewItem = LayoutInflater.from(parent.context).inflate(R.layout.frag_menu_item_card, parent, false)
        return ViewHolder(viewItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      holder.bindItem(menu[position])
    }

    override fun getItemCount(): Int {
         return   menu.size ?: 0
    }

  inner class ViewHolder(itemView: View ): RecyclerView.ViewHolder(itemView){

        fun bindItem(menuItem : MenuItem) {
            val cardView = itemView.findViewById<CardView>(R.id.menu_item_card)
            var name: TextView = itemView.findViewById<TextView>(R.id.menu_item_name)
            var price: TextView = itemView.findViewById<TextView>(R.id.menu_item_price)
            var counter: TextView = itemView.findViewById<TextView>(R.id.counter)
            counter.text= "0";
            val addToOrderBtn = itemView.findViewById<Button>(R.id.btn_add_to_order)
            val incCountBtn = itemView.findViewById<Button>(R.id.btn_increment_counter)
            val decCountBtn = itemView.findViewById<Button>(R.id.btn_decrement_counter)
            addToOrderBtn.tag = menuItem.id
            name.text= menuItem.name
            price.text = menuItem.price.toString()

            // Set listeners for buttons

            addToOrderBtn.setOnClickListener { view ->
                val item = menuViewModel.menuItems.value!!.filter { mi -> mi.id == view.tag }.toList()
                val name = item.get(0).name
                val price = item.get(0).price
                val quantity = counter.text.toString().toLong()
                var pedidoItem = PedidoItem(view.tag as String, name, price, quantity, "pending" ,menuViewModel.currentContentPart.value!!)
                Log.e("PedidoItem: ", pedidoItem.toString())
                Log.e("Actual OrderContent", menuViewModel.contentOrder.value.toString())

                // Decides whether to create a new card in Pedido or accumulate in the previous one
                // Pour ce faire, la fonction prends le state seulement
                if (menuViewModel.contentOrder.value != null) {
                    var filteredList = menuViewModel.contentOrder.value!!.filter {
                            it.id.equals(pedidoItem.id) && it.state.equals(pedidoItem.state) && it.part==pedidoItem.part
                    }.toMutableList()
                    Log.e("FilteredList", filteredList.toString())

                    if(filteredList.size==1){
                        Log.e("IndexOf",menuViewModel.contentOrder.value!!.indexOf(filteredList[0]).toString())
                        menuViewModel.contentOrder.value!![menuViewModel.contentOrder.value!!.indexOf(filteredList[0])].quantity += pedidoItem.quantity
                    }
                    else{
                        menuViewModel.contentOrder.value!!.add(pedidoItem)
                    }

                    }

            }

            incCountBtn.setOnClickListener{
                var flag  = counter.text.toString().toInt() +1
                counter.text= flag.toString()
            }
            decCountBtn.setOnClickListener{
                var flag  = counter.text.toString().toInt()
                if(flag > 0) {
                    counter.text = (flag-1).toString()
                }
            }
            cardView.setOnClickListener {
                val intent = Intent(itemView.context   , DetailMealTwoActivity::class.java)
                itemView.context.startActivity(intent)
            }
        }
    }



}