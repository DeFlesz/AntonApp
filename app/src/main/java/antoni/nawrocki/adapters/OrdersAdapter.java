package antoni.nawrocki.adapters;

import static antoni.nawrocki.db.DBReaderContract.*;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

import antoni.nawrocki.MainActivity;
import antoni.nawrocki.R;
import antoni.nawrocki.db.DBReaderContract;
import antoni.nawrocki.fragments.CourseView;
import antoni.nawrocki.fragments.OrderView;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {
    Context context;
    ArrayList<HashMap<String, String>> orders = new ArrayList<>();

    public OrdersAdapter(Context context, ArrayList<HashMap<String, String>> orders) {
        this.context = context;
        this.orders = orders;
    }

    @NonNull
    @Override
    public OrdersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersAdapter.ViewHolder holder, int position) {
        String title = orders.get(position).get(Courses.COLUMN_NAME_TITLE);
        String date = orders.get(position).get(Orders.COLUMN_NAME_DATE);
        String price = orders.get(position).get(Orders.COLUMN_NAME_PRICE) + " PLN";

        holder.courseName.setText(title);
        holder.orderDate.setText(date);
        holder.price.setText(price);

        holder.itemView.setOnClickListener(v -> {
            String courseID = orders.get(position).get(Orders.COLUMN_NAME_COURSE_ID);
            String ordersID = orders.get(position).get(Orders._ID);

            Bundle bundle = new Bundle();
            bundle.putString(Orders.COLUMN_NAME_COURSE_ID, courseID);
            bundle.putString(Orders._ID, ordersID);
            bundle.putString(Orders.COLUMN_NAME_PRICE, price);

            OrderView orderView = new OrderView();
            orderView.setArguments(bundle);

            FragmentManager fragmentManager = ((MainActivity) context).getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, orderView)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .addToBackStack(null)
                    .setReorderingAllowed(true)
                    .commit();
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView courseName;
        TextView orderDate;
        TextView price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            courseName = itemView.findViewById(R.id.order_list_item_course_name);
            orderDate = itemView.findViewById(R.id.order_lsit_item_order_date);
            price = itemView.findViewById(R.id.order_lsit_item_price);
        }
    }
}
