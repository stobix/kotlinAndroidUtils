package se.stobix.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.view.ContextThemeWrapper

/**
 * Functions that get the theme colors for attribute values for the current context theme
 */
class ColorHandler(val ctx: Context, var defaultColorList: List<Int> = listOf()) {

    /**
     * The resource int value of the theme. Defaults to the theme of [ctx]
     */
    var themeRes: Int? = null

    /**
     * The color to return when the attribute conversion function in [withColorFun] found no color for the attribute
     */
    @Suppress("MemberVisibilityCanBePrivate")
    var missingColor: Int = Color.MAGENTA


    /**
     * The [Resources.Theme] that [ColorHandler] is currently using. Set by [ctx] or via [themeRes].
     */
    val theme: Resources.Theme
        get()  {
            val t = themeRes
            return if (t != null) {
                val wrapper = ContextThemeWrapper(ctx,t)
                wrapper.theme
            } else {
                ctx.theme
            }
        }

    /**
     * @param colorList the List of R.attr values we want to get the colors for
     * @param f a function that takes in a Map<ColorAttrIDs, CorrespondingColor> pair
     */
    fun <A>withColorMap(colorList:List<Int> =defaultColorList, f: (AttrColorMap: Map<Int,Int>) -> A): A {
        val sortedColors = colorList.sorted().toIntArray()
        return withColorMap(sortedColors,f)
    }

    /**
     * Takes in just a single attribute and returns its color.
     * This is highly inefficient compared to the [withColorFun]/[withColorMap] versions.
     * Also, it is broken and apparently does not work ever.
     */
    fun convertOne(attribute: Int) =
        withColorMap(listOf(attribute)){
            it[attribute] ?:missingColor
        }

    // TODO use getColorStateList instead of getColor to get null instead of crashes when the index is not a color!

    /**
     * @param colorList the array of R.attr values we want to get the colors for
     * @param f a function that takes in a Map<ColorAttrIDs, CorrespondingColor> pair
     */
    private fun<A> withColorMap(sortedColorArray: IntArray, f: (AttrColorMap: Map<Int,Int>) -> A) : A {
        val arr =theme.obtainStyledAttributes(sortedColorArray)
        val colorMap = sortedColorArray.map { colorRes ->
            val colorVal =
                    arr.getColor(
                            arr.getIndex(
                                    sortedColorArray.indexOf(
                                            colorRes)
                            ),
                            missingColor)
            colorRes to colorVal
        }.toMap()
        val retval = f(colorMap)
        arr.recycle()
        return retval
    }

    /**
     * @param colorList the list of R.attr values we want to get the colors for
     * @param f a function that takes in a function that takes in the R.attr values given in sortedColorArray and outputs the correct color int value
     */
    fun <A>withColorFun(colorList:List<Int> = defaultColorList, f: ( attrToCol: (attr:Int) -> Int ) -> A) : A{
        val sortedColors = colorList.sorted().toIntArray()
        return withColorFun(sortedColors,f)
    }

    /**
     * @param colorList the array of R.attr values we want to get the colors for
     * @param f a function that takes in a function g that takes in the R.attr values given in sortedColorArray and outputs the correct color int value
     */
    private fun <A>withColorFun(colorList:IntArray, f: ( attrToCol: (attr:Int) -> Int ) -> A):A {
        var functionValid = true
        val arr = theme.obtainStyledAttributes(colorList)
        fun getColor(color: Int): Int{
            if(!functionValid)
                error("function called outside its withColorFun loop")
            return arr.getColor(
                    arr.getIndex(
                            colorList.indexOf(color)),
                    missingColor)
        }
        val retval = f(::getColor)
        arr.also{
            functionValid = false
        }.recycle()
        return retval
    }

    /**
     * @param colorList the List of R.attr values we want to get the colors for
     * @param f a function that takes in a function attrToCol that takes in the R.attr values given in sortedColorArray, a default color value for colors not found, and outputs the correct color int value
     */
    fun <A>withDefColorFun(colorList:List<Int> = defaultColorList, f: ( attrToCol: (attr:Int,defaultCol:Int) -> Int) -> A): A {
        val sortedColors = colorList.sorted().toIntArray()
        return withDefColorFun(sortedColors,f)
    }

    /**
     * @param colorList the array of R.attr values we want to get the colors for
     * @param f a function that takes in a function that takes in the R.attr values given in sortedColorArray, a default color value for colors not found, and outputs the correct color int value
     */
    private fun <A>withDefColorFun(colorList:IntArray, f: (attrToCol: (attr:Int,defaultCol:Int) -> Int) -> A) : A{
        var functionValid = true
        var arr = theme.obtainStyledAttributes(colorList)
        fun getColor(color: Int, defColor: Int): Int{
            if(!functionValid)
                error("function called outside its withColorFun loop")
            return arr.getColor(
                    arr.getIndex(colorList.indexOf(color)),
                    arr.getColor(
                            arr.getIndex(colorList.indexOf(defColor)),
                            missingColor))
        }
        val retVal = f(::getColor)
        arr.also{
                    functionValid = false
                }.recycle()
        return retVal
    }

/*
   fun withThemeColors(theme: Int ){
    }

 */
}