package com.ftinc.gitissues.ui.widget

import `in`.uncod.android.bypass.Bypass
import android.content.Context
import android.support.annotation.DrawableRes
import android.support.design.widget.TabLayout
import android.text.Editable
import android.text.Layout
import android.text.Selection
import android.text.TextWatcher
import android.text.method.ScrollingMovementMethod
import android.transition.TransitionManager
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import butterknife.bindView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ftinc.gitissues.R
import com.ftinc.gitissues.util.ImageSpanTarget
import com.ftinc.gitissues.util.ImeUtils
import timber.log.Timber

/**
 *
 * Project: kotlin-github-issues-client
 * Package: com.ftinc.gitissues.ui.widget
 * Created by drew.heavner on 11/8/16.
 */

class MarkdownInput: RelativeLayout, View.OnClickListener, TabLayout.OnTabSelectedListener {

    /***********************************************************************************************
     *
     * Fields
     *
     */

    val actionScrollView: HorizontalScrollView by bindView(R.id.action_scrollview)
    val actionLayout: LinearLayout by bindView(R.id.action_layout)
    val input: EditText by bindView(R.id.input)
    val preview: TextView by bindView(R.id.preview)
    val actionSend: ImageView by bindView(R.id.action_send)
    val inputCompact: TextView by bindView(R.id.input_placeholder)
    val tabs: TabLayout by bindView(R.id.tabs)

    var expanded: Boolean = false

    private var bypass: Bypass

    /***********************************************************************************************
     *
     * Constructors
     *
     */

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        bypass = Bypass(context, Bypass.Options())
    }

    /***********************************************************************************************
     *
     * View Methods
     *
     */

    override fun onFinishInflate() {
        super.onFinishInflate()

        // Setup click listeners and inflater
        inputCompact.setOnClickListener {
            show()
            input.requestFocus()
            ImeUtils.showIme(input)
        }

        input.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty() && before == 0 && count == 1 && s!![start] == '\n') {
                    // Check the previous line
                    val buffer: Editable = input.text
                    val layout: Layout = input.layout
                    val start = Selection.getSelectionStart(buffer)
                    val startLine = layout.getLineForOffset(start)

                    if(startLine > 0){
                        val prevLine: String = buffer.lines()[startLine-1]

                        Timber.i("Checking previous line: $prevLine")

                        val lineNumberRegex = Regex("^[0-9]+\\.")
                        val bulletRegex = Regex("^\\*(?!\\*)")

                        if(prevLine.contains(lineNumberRegex)){
                            var num = lineNumberRegex.find(prevLine)?.groupValues?.get(0)

                            // Now determine if the previous line is empty, or not
                            val isEmpty = prevLine.replace(num ?: "", "").isNullOrBlank()
                            if(!isEmpty) {
                                num = num?.replace(".", "")
                                val lineNumber = "${num?.toInt() as Int + 1}. "
                                buffer.insert(start, lineNumber)
                            }else{
                                // Delete the previous blank line number
                                val s = layout.getLineStart(startLine-1)
                                val e = s + (num?.length ?: 0)
                                buffer.delete(s, e)
                            }
                        }else if(prevLine.contains(bulletRegex)){
                            var num = bulletRegex.find(prevLine)?.groupValues?.get(0)
                            val isEmpty = prevLine.replace(num ?: "", "").isNullOrBlank()
                            if(!isEmpty) {
                                buffer.insert(start, "* ")
                            }else{
                                val s = layout.getLineStart(startLine-1)
                                val e = s + (num?.length ?: 0)
                                buffer.delete(s, e)
                            }
                        }
                    }
                }
            }
        })

        input.setOnKeyListener { view, i, keyEvent ->
            Timber.d("OnKeyListener($i, ${keyEvent.action})")
            if(keyEvent.action == KeyEvent.ACTION_DOWN){
                when(i){
                    KeyEvent.KEYCODE_ENTER -> {

                    }
                    else -> false
                }
            }
            false
        }

        input.setOnEditorActionListener { textView, i, keyEvent ->
            Timber.d("OnEditorAction($i, ${keyEvent.action})")
            if(i == EditorInfo.IME_ACTION_DONE){

                // Check the previous line
                val buffer: Editable = input.text
                val layout: Layout = input.layout
                val start = Selection.getSelectionStart(buffer)
                val startLine = layout.getLineForOffset(start)

                if(startLine > 0){
                    val prevLine: String = buffer.lines()[startLine-1]

                    Timber.i("Checking previous line: $prevLine")

                    val lineNumberRegex = Regex("^[0-9]+\\.")
                    val bulletRegex = Regex("^\\*")

                    if(prevLine.contains(lineNumberRegex)){
                        var num = lineNumberRegex.find(prevLine)?.groupValues?.get(0)
                        num = num?.replace(".", "")
                        val lineNumber = "${num?.toInt() as Int + 1}. "
                        buffer.insert(start, lineNumber)
                    }else if(prevLine.contains(bulletRegex)){
                        buffer.insert(start, "* ")
                    }

                    true
                }

                false
            }
            false
        }

//        input.setOnFocusChangeListener { view, b ->
//            if(!b){
//                ImeUtils.hideIme(view)
//                hide()
//            }
//        }

        tabs.addOnTabSelectedListener(this)
        preview.movementMethod = ScrollingMovementMethod()

        // Inflate all the actions
        Action.values().forEach {
            val inflater: LayoutInflater = LayoutInflater.from(context)
            val view = inflater.inflate(R.layout.item_markdown_editor_action, actionLayout, false) as ImageView
            actionLayout.addView(view)
            view.setImageResource(it.resId)
            view.tag = it
            view.setOnClickListener(this)
        }

    }

    /***********************************************************************************************
     *
     * Helper Functions
     *
     */

    override fun onClick(v: View?) {
        val action = v?.tag as Action

        // Take the specified action in the input field, accounting for anything 'selected'
        handleAction(action)
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {

    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {

    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        when(tab?.position){
            0 -> hidePreview()
            1 -> showPreview()
        }
    }

    fun show(){
        expanded = true
        TransitionManager.beginDelayedTransition(parent as ViewGroup)
        actionScrollView.visibility = View.VISIBLE
        input.visibility = View.VISIBLE
        preview.visibility = View.GONE
        tabs.visibility = View.VISIBLE
        tabs.setSelectedTabIndicatorColor(0)
        inputCompact.visibility = View.GONE
    }

    fun hide(){
        expanded = false
        TransitionManager.beginDelayedTransition(parent as ViewGroup)
        actionScrollView.visibility = View.GONE
        input.visibility = View.GONE
        preview.visibility = View.GONE
        tabs.visibility = View.GONE
        inputCompact.visibility = View.VISIBLE
    }

    fun showPreview(){
        TransitionManager.beginDelayedTransition(parent as ViewGroup)

        val markdown: String = input.text.toString()
        preview.text = bypass.markdownToSpannable(markdown, preview, { s, imageLoadingSpan ->
            Glide.with(context)
                    .load(s)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ImageSpanTarget(preview, imageLoadingSpan))
        })

        input.visibility = View.INVISIBLE
        preview.visibility = View.VISIBLE
    }

    fun hidePreview(){
        TransitionManager.beginDelayedTransition(parent as ViewGroup)
        input.visibility = View.VISIBLE
        preview.visibility = View.INVISIBLE
    }

    private fun handleAction(action: Action){
        val buffer: Editable = input.text
        val start = Selection.getSelectionStart(buffer)
        val end = Selection.getSelectionEnd(buffer)

        val editorAction: EditorAction = when(action){
            Action.BOLD -> WrapEditorAction("**")
            Action.ITALIC -> WrapEditorAction("_")
            Action.STRIKETHROUGH -> WrapEditorAction("~~")
            Action.QUOTE -> LineWrapEditorAction("> ")
            Action.CODE -> MultilineWrapEditorAction("`", "```")
            Action.MENTION -> ReplaceEditorAction("@")
            Action.LIST_BULLETED -> LineWrapEditorAction("*")
            Action.LIST_NUMBERED -> NumberListEditorAction()
            Action.H1 -> LineWrapEditorAction("#")
            Action.H2 -> LineWrapEditorAction("##")
            Action.H3 -> LineWrapEditorAction("###")
            Action.H4 -> LineWrapEditorAction("####")
            Action.H5 -> LineWrapEditorAction("#####")
            Action.H6 -> LineWrapEditorAction("######")
        }

        editorAction.apply(input)
    }

    /***********************************************************************************************
     *
     * Editor actions
     *
     */

    enum class Action(@DrawableRes val resId: Int){
        BOLD(R.drawable.ic_format_bold),
        ITALIC(R.drawable.ic_format_italic),
        STRIKETHROUGH(R.drawable.ic_format_strikethrough),
//        UNDERLINE(R.drawable.ic_format_underline),
        QUOTE(R.drawable.ic_format_quote),
        CODE(R.drawable.ic_code_brackets),
        MENTION(R.drawable.ic_at),
        LIST_BULLETED(R.drawable.ic_format_list_bulleted),
        LIST_NUMBERED(R.drawable.ic_format_list_numbers),
        H1(R.drawable.ic_format_header_1),
        H2(R.drawable.ic_format_header_2),
        H3(R.drawable.ic_format_header_3),
        H4(R.drawable.ic_format_header_4),
        H5(R.drawable.ic_format_header_5),
        H6(R.drawable.ic_format_header_6)
    }

    abstract class EditorAction(val char: String){
        abstract fun apply(input: EditText)
    }

    class WrapEditorAction(char: String) : EditorAction(char) {

        override fun apply(input: EditText) {
            val buffer: Editable = input.text
            val start = Selection.getSelectionStart(buffer)
            val end = Selection.getSelectionEnd(buffer)

            if(start == end){
                if(start != -1){
                    buffer.insert(start, "$char$char")
                    input.setSelection(start+1)
                }else{
                    buffer.insert(buffer.length, "$char$char")
                    input.setSelection(buffer.length-1)
                }
            }else{
                buffer.insert(end, char)
                buffer.insert(start, char)
            }
        }
    }

    class MultilineWrapEditorAction(char: String, val altChar: String): EditorAction(char) {

        override fun apply(input: EditText) {
            val buffer: Editable = input.text
            val start = Selection.getSelectionStart(buffer)
            val end = Selection.getSelectionEnd(buffer)

            if(start == end){
                if(start != -1){
                    buffer.insert(start, "$char$char")
                    input.setSelection(start+1)
                }else{
                    buffer.insert(buffer.length, "$char$char")
                    input.setSelection(buffer.length-1)
                }
            }else{
                // Insert line at line below end
                val layout = input.layout

                val startLine = layout.getLineForOffset(start)
                val endLine = layout.getLineForOffset(end)

                if(startLine != endLine) {

                    // Now insert a line at 1 + endline
                    buffer.insert(layout.getLineEnd(endLine), "\n")
                    buffer.insert(layout.getLineStart(endLine + 1), altChar)

                    // Now insert a new line at the one before the start line
                    if (startLine == 0) {
                        buffer.insert(0, "\n")
                        buffer.insert(0, altChar)
                    } else {
                        buffer.insert(layout.getLineEnd(startLine - 1), "\n")
                        buffer.insert(layout.getLineStart(startLine), altChar)
                    }

                }else{
                    buffer.insert(end, char)
                    buffer.insert(start, char)
                }
            }
        }

    }

    open class LineWrapEditorAction(char: String) : EditorAction(char){

        protected open fun getLineStartCharacter(input: EditText): String{
            return "$char "
        }

        override fun apply(input: EditText) {
            val buffer: Editable = input.text
            val start = Selection.getSelectionStart(buffer)
            val end = Selection.getSelectionEnd(buffer)

            if(start == end){
                if(start != -1){
                    buffer.insert(start, getLineStartCharacter(input))
                }else {
                    buffer.insert(buffer.length, getLineStartCharacter(input))
                }
            }else{
                val layout: Layout = input.layout
                val lineStart = layout.getLineForOffset(start)
                val lineEnd = layout.getLineForOffset(end)
                if(lineStart != lineEnd) {
                    for (i in lineEnd downTo lineStart) {
                        val lineStartOffset = layout.getLineStart(i)
                        buffer.insert(lineStartOffset, getLineStartCharacter(input))
                    }
                }else if(lineStart != -1){
                    val lineStartOffset = layout.getLineStart(lineStart)
                    buffer.insert(lineStartOffset, getLineStartCharacter(input))
                }else{
                    buffer.insert(buffer.length, char)
                    input.setSelection(buffer.length-1)
                }
            }
        }

    }

    class ReplaceEditorAction(char: String): EditorAction(char) {
        override fun apply(input: EditText) {
            val buffer: Editable = input.text
            val start = Selection.getSelectionStart(buffer)
            val end = Selection.getSelectionEnd(buffer)
            if(start == end){
                if(start != -1){
                    buffer.insert(start, "@")
                }else{
                    buffer.insert(buffer.length, "@")
                }
            }else{
                buffer.replace(start, end, "@")
            }
        }

    }

    class NumberListEditorAction: LineWrapEditorAction("1."){

        private var currentNumber: Int = 1

        override fun getLineStartCharacter(input: EditText): String {
            // Scan to determine if there is
            val buffer: Editable = input.text
            val layout: Layout = input.layout
            val start = Selection.getSelectionStart(buffer)
            val startLine = layout.getLineForOffset(start)

            if(startLine > 0){
                val prevLine: String = buffer.lines()[startLine-1]

                Timber.i("Checking previous line: $prevLine")

                // Use regex to tell if line starts with number
                val regex = Regex("^[0-9]+\\.")
                if(prevLine.contains(regex)){
                    // Huzzah, we found previous current number, adjust current number to that number
                    var num = regex.find(prevLine)?.groupValues?.get(0)
                    num = num?.replace(".", "")

                    Timber.i("Regex matched previous line($prevLine) with ($num)")

                    currentNumber = num?.toInt() as Int + 1
                }
            }

            return "${currentNumber++}. "
        }

        override fun apply(input: EditText) {
            super.apply(input)
        }

    }

}