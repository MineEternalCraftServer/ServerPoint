package mc.mec.serverpoint

import mc.mec.serverpoint.ServerPoint.Companion.dataBase
import mc.mec.serverpoint.ServerPoint.Companion.plugin
import org.bukkit.scheduler.BukkitRunnable
import java.sql.Connection
import java.sql.SQLException
import java.sql.Statement
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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
    fun createPlayer(uuid: String): Boolean {
        val sql = "INSERT INTO server_point (uuid,point,date) VALUES ('${uuid}','0','${getDate()}');"
        try {
            //  DB
            val connection = dataBase.getConnection()
            if (connection == null) {
                dataBase.sendErrorMessage()
                plugin.logger.info("Can not access DB. method:PointAPI/createPlayer")
                return false
            }
            val statement = connection.createStatement()

            //  check exist
            val selectSQL = "SELECT point FROM server_point WHERE uuid='${uuid}' LIMIT 1;"
            val result = statement.executeQuery(selectSQL)
            if (result.next()) {
                statement.close()
                connection.close()
                return true
            }
            result.close()
            //  Insert
            statement.execute(sql)
            statement.close()
            connection.close()

            //  Log
            addLog(LogInfo(uuid, Action.CREATE, -1, getDate()))
            //  Logger
            plugin.logger.info("created player. uuid:$uuid")
        } catch (e: SQLException) {
            e.printStackTrace()
            plugin.logger.info("Can not create player.")
            return false
        }
        return true
    }

    /**
     * function of delete player data
     * @param uuid[String]　UUID
     * @return success[Boolean] success
     */
    fun deletePlayer(uuid: String): Boolean {
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

            //  Log
            addLog(LogInfo(uuid, Action.DELETE, -1, getDate()))
            //  Logger
            plugin.logger.info("deleted player. uuid:$uuid")
        } catch (e: SQLException) {
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
    fun addPoint(uuid: String, amount: Int): Boolean {
        //  Select
        val selectSQL = "SELECT point,date FROM server_point WHERE uuid='${uuid}' LIMIT 1;"
        //  UPDATE
        val updateSQL = "UPDATE server_point SET point=?,date=? WHERE uuid='${uuid}';"

        try {
            //  DB
            val connection = dataBase.getConnection()
            if (connection == null) {
                dataBase.sendErrorMessage()
                plugin.logger.info("Can not access DB. method:PointAPI/addPoint")
                return false
            }
            val statement = connection.createStatement()
            val result = statement.executeQuery(selectSQL)
            var point = if(!result.next()){
                createPlayer(uuid)
                0
            }else {
                result.getInt("point")
            }

            //  close resultSet
            result.close()

            val pst = connection.prepareStatement(updateSQL)
            //  add point
            point += amount
            //  replace
            pst.setInt(1,point)
            pst.setString(2, getDate())
            pst.executeUpdate()

            //  Close
            statement.close()
            pst.close()
            connection.close()

            //  Log
            addLog(LogInfo(uuid, Action.ADD, amount, getDate()))
            //  Logger
            plugin.logger.info("added point. uuid:$uuid added: $amount")
        } catch (e: SQLException) {
            e.printStackTrace()
            plugin.logger.info("Can not add point.")
            return false
        }
        return true
    }

    /**
     * function of remove point
     * @param uuid[String] uuid
     * @param amount[Int] point amount
     * @return success[Boolean] success
     */
    fun removePoint(uuid: String, amount: Int): Boolean {
        //  Select
        val selectSQL = "SELECT point,date FROM server_point WHERE uuid='${uuid}' LIMIT 1;"
        //  UPDATE
        val updateSQL = "UPDATE server_point SET point=?,date=? WHERE uuid='${uuid}';"

        try {
            //  DB
            val connection = dataBase.getConnection()
            if (connection == null) {
                dataBase.sendErrorMessage()
                plugin.logger.info("Can not access DB. method:PointAPI/removePoint")
                return false
            }
            val statement = connection.createStatement()
            val result = statement.executeQuery(selectSQL)
            var point: Int = if (!result.next()) {
                //  Create Player Data
                createPlayer(uuid)
                0
            } else{
                result.getInt("point")
            }
            //  close resultSet
            result.close()

            val pst = connection.prepareStatement(updateSQL)
            //  add point
            point -= amount
            //  replace
            pst.setInt(1,point)
            pst.setString(2, getDate())
            pst.executeUpdate()

            //  Close
            statement.close()
            pst.close()
            connection.close()

            //  Log
            addLog(LogInfo(uuid, Action.REMOVE, amount, getDate()))
            //  Logger
            plugin.logger.info("added point. uuid:$uuid added: $amount")
        } catch (e: SQLException) {
            e.printStackTrace()
            plugin.logger.info("Can not add point.")
            return false
        }
        return true
    }

    /**
     * function of get point
     * @param uuid[String] uuid
     * @return point[Int] point (if throw exception,return Int -1)
     */
    fun getPoint(uuid: String): Int {
        var amount = 0
        //  Select
        val selectSQL = "SELECT point,date FROM server_point WHERE uuid='${uuid}' LIMIT 1;"

        try {
            //  DB
            val connection = dataBase.getConnection()
            if (connection == null) {
                dataBase.sendErrorMessage()
                plugin.logger.info("Can not access DB. method:PointAPI/getPoint")
                return -0
            }
            val statement = connection.createStatement()
            val result = statement.executeQuery(selectSQL)
            amount = if (!result.next()) {
                //  Create Player Data
                createPlayer(uuid)
                0
            } else {
                result.getInt("point")
            }
            //  close resultSet
            result.close()
            //  Close
            statement.close()
            connection.close()
        } catch (e: SQLException) {
            e.printStackTrace()
            plugin.logger.info("Can not get point data.")
            return -1
        }
        return amount
    }

    /**
     * function of create table
     * @return success[Boolean]
     */
    fun createTable(): Boolean {
        val sql = "create table if not exists server_point\n" +
                "(\n" +
                "\tid int auto_increment,\n" +
                "\tuuid VARCHAR(36) not null,\n" +
                "\tpoint int default 0 null,\n" +
                "\tdate datetime not null,\n" +
                "\tconstraint server_point_pk\n" +
                "\t\tprimary key (id)\n" +
                ");"
        val sql_log = "create table if not exists server_point_log\n" +
                "(\n" +
                "\tid int auto_increment,\n" +
                "\tuuid VARCHAR(36) not null,\n" +
                "\taction VARCHAR(50) not null,\n" +
                "\tpoint int not null,\n" +
                "\tdate datetime not null,\n" +
                "\tconstraint server_point_log_pk\n" +
                "\t\tprimary key (id)\n" +
                ");"
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
            statement.execute(sql_log)
            statement.close()
            connection.close()
            //  Logger
            plugin.logger.info("created table.")
        } catch (e: SQLException) {
            e.printStackTrace()
            plugin.logger.info("Can not create table.")
            return false
        }
        return true
    }

    private enum class Action {
        CREATE, DELETE, ADD, REMOVE
    }

    private data class LogInfo(val uuid: String, val action: Action, val point: Int, val date: String)
    //////////////////////////////
    //  LOG
    /**
     * function of add logs.
     * @param info[LogInfo] information data class
     */
    private fun addLog(info: LogInfo) {
        object : BukkitRunnable() {
            override fun run() {
                val sql = when (info.action) {
                    Action.CREATE -> {
                        "INSERT INTO server_point_log (uuid,action,point,date) VALUES ('${info.uuid}','CREATE','-1','${info.date}');"
                    }
                    Action.DELETE -> {
                        "INSERT INTO server_point_log (uuid,action,point,date) VALUES ('${info.uuid}','DELETE','-1','${info.date}');"
                    }
                    Action.ADD -> {
                        "INSERT INTO server_point_log (uuid,action,point,date) VALUES ('${info.uuid}','ADD','${info.point}','${info.date}');"
                    }
                    Action.REMOVE -> {
                        "INSERT INTO server_point_log (uuid,action,point,date) VALUES ('${info.uuid}','REMOVE','${info.point}','${info.date}');"
                    }
                }
                try {
                    //  DB
                    val connection = dataBase.getConnection()
                    if (connection == null) {
                        dataBase.sendErrorMessage()
                        plugin.logger.info("Can not access DB. method:PointAPI/addLog")
                        return
                    }
                    val statement = connection.createStatement()
                    statement.execute(sql)
                    statement.close()
                    connection.close()
                } catch (e: SQLException) {
                    e.printStackTrace()
                    plugin.logger.info("Can not add log.")
                }
            }
        }.runTask(plugin)
    }

    //  Unit
    //  get date
    private fun getDate(): String {
        //  date
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return current.format(formatter)
    }
}