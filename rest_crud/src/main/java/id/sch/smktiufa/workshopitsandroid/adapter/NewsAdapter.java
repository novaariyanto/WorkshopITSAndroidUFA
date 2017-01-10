package id.sch.smktiufa.workshopitsandroid.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import id.sch.smktiufa.workshopitsandroid.R;
import id.sch.smktiufa.workshopitsandroid.activity.ActivityNews;
import id.sch.smktiufa.workshopitsandroid.entity.News;

/**
 * Created by smktiufa on 16/12/16.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private Context context;
    private List<News> newslist = new ArrayList<>();
    private SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm");

    public NewsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_news, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Date date = new Date(newslist.get(position).getCreateDate());

        holder.title.setText(newslist.get(position).getTitle());
        holder.date.setText(dateformat.format(date));
        holder.content.setText(newslist.get(position).getContent());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                dialog(position);
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return newslist.size();
    }

    public void addNews(News news) {
        newslist.add(news);
        notifyDataSetChanged();
    }


    public void addNewses(List<News> newsList) {
        this.newslist.addAll(newsList);
        notifyDataSetChanged();
    }

    public void clearNews() {
        newslist.clear();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView date;
        TextView content;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.text_title);
            date = (TextView) itemView.findViewById(R.id.text_date);
            content = (TextView) itemView.findViewById(R.id.text_content);
        }

    }

    private void dialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setItems(
                new String[]{
                        context.getString(R.string.text_edit),
                        context.getString(R.string.text_delete)
                },
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            ((ActivityNews)context).editNews(newslist.get(position));
                        } else if (i == 1) {

                            dialogdelete(position);
                        }
                    }
                });
        builder.show();

    }
    private void dialogdelete(final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage(R.string.text_message_delete);

        builder.setPositiveButton(R.string.text_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((ActivityNews)context).deletenews(newslist.get(position));
            }
        });
        builder.setNegativeButton(R.string.text_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.show();
    }
}
