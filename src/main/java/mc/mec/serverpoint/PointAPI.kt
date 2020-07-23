package mc.mec.serverpoint

import mc.mec.serverpoint.ServerPoint.Companion.dataBase
import mc.mec.serverpoint.ServerPoint.Companion.plugin
import java.sql.SQLException
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
        val sql = "DELETE FROM server_point WHERE uuid='${uuid}';"
        try {
            //  DB
            val connection = dataBase.getConnection()
            if (connection == null) {
                dataBase.sendErrorMessage()
                plugin.logger.info("Can not access DB. method:PointAPI/deletePlayer")
                return false
            }
            val statement = connection.createStatement()
            statement.execute(sql)
            statement.close()
            connection.close()
            //  Logger
            plugin.logger.info("deleted player. uuid:$uuid")
        }catch (e:SQLException){
            e.printStackTrace()
            plugin.logger.info("Can not delete player.")
            return false
        }
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

    /**
     * function of create table
     * @return success[Boolean]
     */
    fun createTable():Boolean {
        val sql = "create table server_point\n" +
                "(\n" +
                "\tid int auto_increment,\n" +
                "\tuuid VARCHAR(36) not null,\n" +
                "\tpoint int default 0 null,\n" +
                "\tdate datetime not null,\n" +
                "\tconstraint server_point_pk\n" +
                "\t\tprimary key (id)\n" +
                ");\n" +
                "\n" +
                "create index server_point_uuid_index\n" +
                "\ton server_point (uuid);"
        try {
            //  DB
            val connection = dataBase.getConnection()
            if (connection == null) {
                dataBase.sendErrorMessage()
                plugin.logger.info("Can not access DB. method:PointAPI/createTable")
                return false
            }
            val statement = connection.createStatement()
            statement.execute(sql)
            statement.close()
            connection.close()
            //  Logger
            plugin.logger.info("created table.")
        }catch (e:SQLException){
            e.printStackTrace()
            plugin.logger.info("Can not create table.")
            return false
        }
        return true
    }
}