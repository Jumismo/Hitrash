package hitrash.jumismo.android.uoc.edu.hitrash;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import hitrash.jumismo.android.uoc.edu.hitrash.Model.Comment;

public class CommentConversationArrayAdapter extends  RecyclerView.Adapter<CommentConversationArrayAdapter.ViewHolder>{

    List<Comment> commentList;

    public CommentConversationArrayAdapter(List<Comment> commentList) {
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public CommentConversationArrayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_comment_fragment, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentConversationArrayAdapter.ViewHolder viewHolder, int i) {
        Comment comment = commentList.get(i);
        viewHolder.comment = comment;
        viewHolder.textComment.setText(comment.getComment());
        viewHolder.publicationDate.setText(comment.getPublicationDate().toString());
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private Comment comment;
        private TextView publicationDate;
        private TextView textComment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            publicationDate = (TextView) itemView.findViewById(R.id.publicationDateConversation);
            textComment = (TextView)itemView.findViewById(R.id.commentConversation);

        }
    }
}
