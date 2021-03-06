package ru.ozh.recycler.decorator.chat.decor

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ozh.bunchdecorator.example.R
import kotlinx.android.synthetic.main.chat_message_layout.view.*
import ru.ozh.recycler.decorator.chat.ChatMessageDirection
import ru.ozh.recycler.decorator.chat.controller.ChatMessageController
import ru.surfstudio.android.recycler.decorator.Decorator
import ru.ozh.recycler.decorator.px

class ChatMessageDecor(private val context: Context) : Decorator.ViewHolderDecor {

    private val outcomeBubbleColor = ContextCompat.getColor(context, R.color.material_50)
    private val incomeBubbleColor = ContextCompat.getColor(context, R.color.material_501)

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val outcomeBubbleCorners =
            floatArrayOf(
                    8.px.toFloat(), 8.px.toFloat(),     // Top left radius in px
                    0f, 0f,                                 // Top right radius in px
                    8.px.toFloat(), 8.px.toFloat(),     // Bottom right radius in px
                    0f, 0f                                  // Bottom left radius in px
            )

    private val incomeBubbleCorners =
            floatArrayOf(
                    0f, 0f,                                 // Top left radius in px
                    8.px.toFloat(), 8.px.toFloat(),     // Top right radius in px
                    0f, 0f,                                 // Bottom right radius in px
                    8.px.toFloat(), 8.px.toFloat()      // Bottom left radius in px
            )

    private val path = Path()

    override fun draw(canvas: Canvas, view: View, recyclerView: RecyclerView, state: RecyclerView.State) {

        //take view of ViewHolder for draw message bubble
        val messageAreaView = view.message_area_layout
        val messageAreaRect = RectF(
                messageAreaView.left.toFloat(),
                view.top.toFloat(),
                messageAreaView.right.toFloat(),
                view.bottom.toFloat()
        )

        path.reset()
        val vh = recyclerView.getChildViewHolder(view)
        val nextVh = recyclerView.findViewHolderForAdapterPosition(vh.adapterPosition + 1)

        val isLastInGroup = nextVh !is ChatMessageController.Holder

        if ((vh as ChatMessageController.Holder).chatMessageDirection == ChatMessageDirection.INCOME) {
            paint.color = incomeBubbleColor

            //draw bubble on message background
            path.addRoundRect(messageAreaRect, incomeBubbleCorners, Path.Direction.CW)

            //draw triangle for last one message in group
            if (isLastInGroup) {
                //right bottom corner of bubble
                val (x, y) = messageAreaRect.right to messageAreaRect.bottom
                path.moveTo(x, y)
                path.lineTo(x + 8.px, y)
                path.lineTo(x, y - 8.px)
                path.close()
            }

        } else {
            paint.color = outcomeBubbleColor
            //draw bubble on message background
            path.addRoundRect(messageAreaRect, outcomeBubbleCorners, Path.Direction.CW)

            //draw triangle for last one message in group
            if (isLastInGroup) {
                //left bottom corner of bubble
                val (x, y) = messageAreaRect.left to messageAreaRect.bottom
                path.moveTo(x, y)
                path.lineTo(x - 8.px, y)
                path.lineTo(x, y - 8.px)
                path.close()
            }
        }
        canvas.drawPath(path, paint)
    }
}