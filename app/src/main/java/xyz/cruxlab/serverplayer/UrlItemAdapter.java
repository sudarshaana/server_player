package xyz.cruxlab.serverplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class UrlItemAdapter extends RecyclerView.Adapter<UrlItemAdapter.ViewHolder> {

    private List<String> list;
    private Context context;

    public UrlItemAdapter(Context context, List<String> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.url_layout, parent, false));
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        String item = list.get(position);
        holder.title.setText((position + 1) + ". " + item);


        holder.itemView.setOnClickListener(v -> {
            //onItemClick.onItemClick(position);
            WebLoader.start(context, item);
        });

//        holder.textView.setText(item.getTitle() + "(" + item.getNumberCount() + ")");
//        holder.textView.setOnClickListener(v -> {
//            onChildItemClick.onChipClickListener(item);
//            int previousItem = selectedItem;
//            selectedItem = position;
//            notifyItemChanged(previousItem);
//            notifyItemChanged(position);
//        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.text);
        }
    }

//    public void setData(List<String> childList) {
//        if (list.size() != 0) {
//            list.clear();
//        }
//        list.addAll(childList);
//        notifyDataSetChanged();
//    }

}