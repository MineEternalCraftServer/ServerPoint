package mc.mec.serverpoint

import mc.mec.serverpoint.ServerPoint.Companion.plugin
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * ポイント管理のクラスです。
 */
object PointAPI {
    val mysql = MySQLManager(plugin, "serverpoint")

    fun createPlayer(mcid: String, uuid: String): Boolean{
        try {
            val sql = mysql.query("INSERT INTO server_point (mcid,uuid,point,date) VALUES ('${mcid}','${uuid}','0','${getDate()}');")

            sql?.next()
            if (sql == null){
                mysql.close()
                Bukkit.broadcastMessage("§c§l[!]§eデーターベースの不具合です。運営に報告してください。")
                return false
            }

            mysql.execute("SELECT point FROM server_point WHERE mcid='${mcid} 'uuid='${uuid}' LIMIT 1;")

            //  Log
            addLog(LogInfo(mcid, uuid, Action.CREATE, -1, getDate()))

        }catch (e: ){
        }

    }

    enum class Action {
        CREATE, DELETE, ADD, REMOVE
    }
    data class LogInfo(val mcid: String, val uuid: String, val action: Action, val point: Int, val date: String)

    fun addLog(info: LogInfo){
        when (info.action){
            Action.CREATE -> {
                "INSERT INTO server_point_log (uuid,action,point,date) VALUES ('${info.mcid}','${info.uuid}','CREATE','-1','${info.date}');"
            }
            Action.DELETE -> {
                "INSERT INTO server_point_log (uuid,action,point,date) VALUES ('${info.mcid}','${info.uuid}','DELETE','-1','${info.date}');"
            }
            Action.ADD -> {
                "INSERT INTO server_point_log (uuid,action,point,date) VALUES ('${info.mcid}','${info.uuid}','ADD','${info.point}','${info.date}');"
            }
            Action.REMOVE -> {
                "INSERT INTO server_point_log (uuid,action,point,date) VALUES ('${info.mcid}','${info.uuid}','REMOVE','${info.point}','${info.date}');"
            }
        }
        try {
            
        }
    }

    fun getDate(): String {
        //  date
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return current.format(formatter)
    }
}