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
import com.ftinc.gitissues.util.*
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

    val LINE_NUMBER_REGEX: Regex = Regex("^\\s*?[0-9]+\\.")
    val BULLET_REGEX: Regex = Regex("^\\*(?!\\*)")
    val INDENT_REGEX: Regex = Regex("^(\\s{4}|\\t)")
    val MULTI_INDENT_REGEX: Regex = Regex("^(\\s{4}|\\t)+")
    val INDENT: String = "    "

    val actionScrollView: HorizontalScrollView by bindView(R.id.action_scrollview)
    val actionLayout: LinearLayout by bindView(R.id.action_layout)
    val input: EditText by bindView(R.id.input)
    val preview: TextView by bindView(R.id.preview)
    val actionSend: ImageView by bindView(R.id.action_send)
    val loading: ProgressBar by bindView(R.id.loading)
    val inputCompact: TextView by bindView(R.id.input_placeholder)
    val tabs: TabLayout by bindView(R.id.tabs)

    var expanded: Boolean = false

    private var bypass: Bypass
    private var onMarkdownSubmitListener: OnMarkdownSubmitListener? = null

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

    fun setOnMarkdownSubmitListener(listener: OnMarkdownSubmitListener){
        onMarkdownSubmitListener = listener
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
            override fun afterTextChanged(s: Editable?) {
                actionSend.isEnabled = !s.isNullOrBlank()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty() && before == 0 && count == 1 && s!![start] == '\n') {
                    // Check the previous line
                    val buffer: Editable = input.text
                    val layout: Layout = input.layout
                    val startOffset = Selection.getSelectionStart(buffer)
                    val startLine = layout.getLineForOffset(startOffset)

                    if(startLine > 0){
                        val prevLine: String = buffer.lines()[startLine-1]

                        Timber.i("Checking previous line: $prevLine")

                        if(prevLine.contains(LINE_NUMBER_REGEX)){
                            var num = LINE_NUMBER_REGEX.find(prevLine)?.groupValues?.get(0)

                            // Now determine if the previous line is empty, or not
                            val isEmpty = prevLine.replace(num ?: "", "").isNullOrBlank()
                            if(!isEmpty) {
                                num = num?.replace(".", "")?.trim()
                                val lineNumber = "${num?.toInt() as Int + 1}. "

                                // Now determine if that previous line has an indent(s) and then prepend the amount to the newline
                                if(prevLine.contains(MULTI_INDENT_REGEX)){
                                    val indent = MULTI_INDENT_REGEX.find(prevLine)?.value
                                    buffer.insert(startOffset, "$indent$lineNumber")
                                }else {
                                    buffer.insert(startOffset, lineNumber)
                                }

                            }else{
                                // Delete the previous blank line number
                                val s = layout.getLineStart(startLine-1)
                                val e = s + (num?.length ?: 0)
                                buffer.delete(s, e)
                            }
                        }else if(prevLine.contains(BULLET_REGEX)){
                            var num = BULLET_REGEX.find(prevLine)?.groupValues?.get(0)
                            val isEmpty = prevLine.replace(num ?: "", "").isNullOrBlank()
                            if(!isEmpty) {
                                if(prevLine.contains(MULTI_INDENT_REGEX)){
                                    val indent = MULTI_INDENT_REGEX.find(prevLine)?.value
                                    buffer.insert(startOffset, "$indent$* ")
                                }else {
                                    buffer.insert(startOffset, "* ")
                                }
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

        // setup action send input
        actionSend.setOnClickListener {
            onMarkdownSubmitListener?.onMarkdownSubmit(input.text.toString())
            loading.visible()
        }

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
            view.setOnLongClickListener {
                val t: Toast = Toast.makeText(context, (it.tag as Action).name, Toast.LENGTH_SHORT)
                Tools.positionToast(t, it, 0, 0)
                t.show()
                true
            }
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
        Tools.visibility(true, actionScrollView, input, tabs)
        Tools.visibility(false, preview, inputCompact)
        tabs.setSelectedTabIndicatorColor(0)
        actionSend.isEnabled = input.text.isNullOrBlank()
    }

    fun hide(){
        expanded = false
        TransitionManager.beginDelayedTransition(parent as ViewGroup)
        Tools.visibility(false, actionScrollView, input, preview, tabs, loading)
        Tools.visibility(true, inputCompact)
        actionSend.isEnabled = false
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

        input.invisible()
        preview.visible()
    }

    fun hidePreview(){
        TransitionManager.beginDelayedTransition(parent as ViewGroup)
        input.visible()
        preview.invisible()
    }

    private fun handleAction(action: Action){
        if(input.visibility == View.VISIBLE) {
            val editorAction: EditorAction = when (action) {
                Action.BOLD -> WrapEditorAction("**")
                Action.ITALIC -> WrapEditorAction("_")
                Action.STRIKETHROUGH -> WrapEditorAction("~~")
                Action.QUOTE -> LineWrapEditorAction("> ")
                Action.CODE -> MultilineWrapEditorAction("`", "```")
                Action.LINK -> LinkEditorAction()
                Action.MENTION -> ReplaceEditorAction("@")
                Action.LIST_BULLETED -> LineWrapEditorAction("*")
                Action.LIST_NUMBERED -> NumberListEditorAction()
                Action.H1 -> LineWrapEditorAction("#")
                Action.H2 -> LineWrapEditorAction("##")
                Action.H3 -> LineWrapEditorAction("###")
                Action.H4 -> LineWrapEditorAction("####")
                Action.H5 -> LineWrapEditorAction("#####")
                Action.H6 -> LineWrapEditorAction("######")
                Action.INDENT_LEFT -> IndentEditorAction(false)
                Action.INDENT_RIGHT -> IndentEditorAction(true)
            }

            editorAction.apply(input)
        }
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
        CODE(R.drawable.ic_code_braces),
        LINK(R.drawable.ic_link_variant),
        MENTION(R.drawable.ic_at),
        LIST_BULLETED(R.drawable.ic_format_list_bulleted),
        LIST_NUMBERED(R.drawable.ic_format_list_numbers),
        H1(R.drawable.ic_format_header_1),
        H2(R.drawable.ic_format_header_2),
        H3(R.drawable.ic_format_header_3),
        H4(R.drawable.ic_format_header_4),
        H5(R.drawable.ic_format_header_5),
        H6(R.drawable.ic_format_header_6),
        INDENT_RIGHT(R.drawable.ic_format_indent_increase),
        INDENT_LEFT(R.drawable.ic_format_indent_decrease)

    }

    abstract class EditorAction(val char: String){
        abstract fun apply(input: EditText)
    }

    class LinkEditorAction : EditorAction("") {
        override fun apply(input: EditText) {
            val buffer = input.text
            val layout = input.layout
            val start = Selection.getSelectionStart(buffer)
            val end = Selection.getSelectionEnd(buffer)
            val startLine = layout.getLineForOffset(start)
            val endLine = layout.getLineForOffset(end)

            if(start == end){
                if(start != -1){
                    buffer.insert(start, "[]()")
                    input.setSelection(start + 1)
                }else{
                    val len = buffer.length
                    buffer.insert(len, "[]()")
                    input.setSelection(len+1)
                }
            }else{
                if(startLine == endLine){
                    buffer.insert(end, "]()")
                    buffer.insert(start, "[")
                    input.setSelection(end+3)
                }else{
                    buffer.replace(start, end, "[]()")
                    input.setSelection(start+1)
                }
            }
        }

    }

    inner class IndentEditorAction(val increase: Boolean): EditorAction("") {
        override fun apply(input: EditText) {
            val buffer = input.text
            val layout = input.layout
            val start = Selection.getSelectionStart(buffer)
            val end = Selection.getSelectionEnd(buffer)
            val startLine = layout.getLineForOffset(start)
            val endLine = layout.getLineForOffset(end)

            if(start != -1){
                if(start == end){
                    // if increase, add 4 spaces to start of line
                    if(increase) {
                        val s = layout.getLineStart(startLine)
                        buffer.insert(s, INDENT)
                    }else{
                        val line = buffer.lines()[startLine]
                        if(line.contains(INDENT_REGEX)){
                            val match = INDENT_REGEX.find(line)?.groupValues?.get(0)

                            val s = layout.getLineStart(startLine)
                            buffer.delete(s, s + (match?.length ?: 0))
                        }
                    }
                }else{
                    if(increase){
                        (endLine downTo startLine)
                                .map { layout.getLineStart(it) }
                                .forEach { buffer.insert(it, INDENT) }
                    }else{
                        for(i in endLine downTo startLine){
                            val line = buffer.lines()[i]
                            if(line.contains(INDENT_REGEX)){
                                val match = INDENT_REGEX.find(line)?.groupValues?.get(0)

                                val s = layout.getLineStart(i)
                                buffer.delete(s, s + (match?.length ?: 0))
                            }
                        }
                    }
                }

            }
        }
    }

    class WrapEditorAction(char: String) : EditorAction(char) {

        override fun apply(input: EditText) {
            val buffer: Editable = input.text
            val start = Selection.getSelectionStart(buffer)
            val end = Selection.getSelectionEnd(buffer)

            if(start == end){
                if(start != -1){
                    buffer.insert(start, "$char$char")
                    input.setSelection(start+char.length)
                }else{
                    buffer.insert(buffer.length, "$char$char")
                    input.setSelection(buffer.length-char.length)
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
                    input.setSelection(start+char.length)
                }else{
                    buffer.insert(buffer.length, "$char$char")
                    input.setSelection(buffer.length-char.length)
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

    inner class NumberListEditorAction: LineWrapEditorAction("1."){

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
                if(prevLine.contains(LINE_NUMBER_REGEX)){
                    // Huzzah, we found previous current number, adjust current number to that number
                    var num = LINE_NUMBER_REGEX.find(prevLine)?.groupValues?.get(0)
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

    interface OnMarkdownSubmitListener{
        fun onMarkdownSubmit(markdown: String)
    }

}