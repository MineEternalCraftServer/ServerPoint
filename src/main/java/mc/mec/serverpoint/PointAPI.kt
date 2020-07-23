package mc.mec.serverpoint

import java.util.*

/**
 * Created by testusuke on 2020/07/23
 * @author testusuke
 */

/**
 * ポイント管理のクラスです。
 */
object PointAPI {

    /**
     * function of create player data
     * @param uuid[String]　UUID
     * @return success[Boolean] success
     */
    fun createPlayer(uuid: String):Boolean{


        return true
    }

    /**
     * function of delete player data
     * @param uuid[String]　UUID
     * @return success[Boolean] success
     */
    fun deletePlayer(uuid: String):Boolean{


        return true
    }

    /**
     * function of add point
     * @param uuid[String] uuid
     * @param amount[Int] point amount
     * @return success[Boolean] success
     */
    fun addPoint(uuid:String,amount:Int):Boolean{


        return true
    }

    /**
     * function of remove point
     * @param uuid[String] uuid
     * @param amount[Int] point amount
     * @return success[Boolean] success
     */
    fun removePoint(uuid:String,amount:Int):Boolean{


        return true
    }

    /**
     * function of get point
     * @param uuid[String] uuid
     * @return point[Int] point (if throw exception,return Int -1)
     */
    fun getPoint(uuid:String):Int{
        var amount = 0

        return amount
    }

}