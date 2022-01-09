package ru.sasha77.orthotester

import java.lang.IllegalArgumentException

private val matrix = listOf(
    listOf(-1000F,-2F,-1F,0F,2F,5F,7F,9F,12F,14F,16F,18F,21F,23F,25F,27F,29F,31F,33F),
    listOf(42F,14.5F,14F,13.5F,13F,12.5F,12F,11.5F,11F,10.5F,10F,9.5F,9F,8.5F,8F,7.5F,7F,6.5F,6F),
    listOf(46F,14F,13.5F,13F,12.5F,12F,11.5F,11F,10.5F,10F,9.5F,9F,8.5F,8F,7.5F,7F,6.5F,6F,5.5F),
    listOf(49F,13.5F,13F,12.5F,12F,11.5F,11F,10.5F,10F,9.5F,9F,8.5F,8F,7.5F,7F,6.5F,6F,5.5F,5F),
    listOf(52F,13F,12.5F,12F,11.5F,11F,10.5F,10F,9.5F,9F,8.5F,8F,7.5F,7F,6.5F,6F,5.5F,5F,4.5F),
    listOf(55F,12.5F,12F,11.5F,11F,10.5F,10F,9.5F,9F,8.5F,8F,7.5F,7F,6.5F,6F,5.5F,5F,4.5F,4F),
    listOf(58F,12F,11.5F,11F,10.5F,10F,9.5F,9F,8.5F,8F,7.5F,7F,6.5F,6F,5.5F,5F,4.5F,4F,3.5F),
    listOf(62F,11.5F,11F,10.5F,10F,9.5F,9F,8.5F,8F,7.5F,7F,6.5F,6F,5.5F,5F,4.5F,4F,3.5F,3F),
    listOf(65F,10.5F,10F,9.5F,9F,8.5F,8F,7.5F,7F,6.5F,6F,5.5F,5F,4.5F,4F,3.5F,3F,2.5F,2F),
    listOf(68F,10.5F,10F,9.5F,9F,8.5F,8F,7.5F,7F,6.5F,6F,5.5F,5F,4.5F,4F,3.5F,3F,2.5F,2F),
    listOf(71F,10F,9.5F,9F,8.5F,8F,7.5F,7F,6.5F,6F,5.5F,5F,4.5F,4F,3.5F,3F,2.5F,2F,1.5F),
    listOf(74F,9.5F,9F,8.5F,8F,7.5F,7F,6.5F,6F,5.5F,5F,4.5F,4F,3.5F,3F,2.5F,2F,1.5F,1F),
    listOf(77F,9F,8.5F,8F,7.5F,7F,6.5F,6F,5.5F,5F,4.5F,4F,3.5F,3F,2.5F,2F,1.5F,1F,0.5F),
    listOf(80F,8.5F,8F,7.5F,7F,6.5F,6F,5.5F,5F,4.5F,4F,3.5F,3F,2.5F,2F,1.5F,1F,0.5F,0F),
    listOf(83F,8F,7.5F,7F,6.5F,6F,5.5F,5F,4.5F,4F,3.5F,3F,2.5F,2F,1.5F,1F,0.5F,0F,-0.5F),
    listOf(86F,7.5F,7F,6.5F,6F,5.5F,5F,4.5F,4F,3.5F,3F,2.5F,2F,1.5F,1F,0.5F,0F,-0.5F,-1F),
    listOf(89F,7F,6.5F,6F,5.5F,5F,4.5F,4F,3.5F,3F,2.5F,2F,1.5F,1F,0.5F,0F,-0.5F,-1F,-1.5F),
    listOf(93F,6.5F,6F,5.5F,5F,4.5F,4F,3.5F,3F,2.5F,2F,1.5F,1F,0.5F,0F,-0.5F,-1F,-1.5F,-2F),
    listOf(96F,6F,5.5F,5F,4.5F,4F,3.5F,3F,2.5F,2F,1.5F,1F,0.5F,0F,-0.5F,-1F,-1.5F,-2F,-2.5F),
    listOf(99F,5.5F,5F,4.5F,4F,3.5F,3F,2.5F,2F,1.5F,1F,0.5F,0F,-0.5F,-1F,-1.5F,-2F,-2.5F,-3F),
    listOf(102F,5F,4.5F,4F,3.5F,3F,2.5F,2F,1.5F,1F,0.5F,0F,-0.5F,-1F,-1.5F,-2F,-2.5F,-3F,-3.5F),
    listOf(105F,4.5F,4F,3.5F,3F,2.5F,2F,1.5F,1F,0.5F,0F,-0.5F,-1F,-1.5F,-2F,-2.5F,-3F,-3.5F,-4F),
    listOf(109F,4F,3.5F,3F,2.5F,2F,1.5F,1F,0.5F,0F,-0.5F,-1F,-1.5F,-2F,-2.5F,-3F,-3.5F,-4F,-4.5F),
    listOf(112F,3.5F,3F,2.5F,2F,1.5F,1F,0.5F,0F,-0.5F,-1F,-1.5F,-2F,-2.5F,-3F,-3.5F,-4F,-4.5F,-5F),
    listOf(116F,3F,2.5F,2F,1.5F,1F,0.5F,0F,-0.5F,-1F,-1.5F,-2F,-2.5F,-3F,-3.5F,-4F,-4.5F,-5F,-5.5F),
    listOf(118F,2.5F,2F,1.5F,1F,0.5F,0F,-0.5F,-1F,-1.5F,-2F,-2.5F,-3F,-3.5F,-4F,-4.5F,-5F,-5.5F,-6F),
    listOf(121F,2F,1.5F,1F,0.5F,0F,-0.5F,-1F,-1.5F,-2F,-2.5F,-3F,-3.5F,-4F,-4.5F,-5F,-5.5F,-6F,-6.5F)
)
private const val MIN_PULSE = 39F
private const val MAX_PULSE = 121F
private const val MIN_DELTA = -4F
private const val MAX_DELTA  = 33F

fun compute (pulse1:Float, pulse2:Float) : Float {
    val delta = pulse2 - pulse1
    if (pulse1 < MIN_PULSE) throw IllegalArgumentException("pulse_low")
    if (pulse1 > MAX_PULSE) throw IllegalArgumentException("pulse_high")
    if (delta  < MIN_DELTA || delta  > MAX_DELTA) throw IllegalArgumentException("delta_high")
    var x = 0;var y = 0
    for (i in 0 until matrix[0].size) if (matrix[0][i] >= delta) {x = i;break}
    for (i in 0 until matrix.size) if (matrix[i][0] >= pulse1) {y = i;break}
    if (x>0&&y>0) return matrix[y][x] else throw IllegalStateException()
}