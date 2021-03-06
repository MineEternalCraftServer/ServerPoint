package mc.mec.serverpoint

import mc.mec.serverpoint.ServerPoint.Companion.plugin
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

/**
 * Created by testusuke on 2020/07/23
 * @author testusuke
 */
class DataBase(private val prefix: String) {

    //  Connect information
    private var host: String? = null
    private var user: String? = null
    private var pass: String? = null
    private var port: String? = null
    private var db: String? = null

    //  エラーモード
    private var errorMode = false

    init {
        //  Logger
        plugin.logger.info("DataBaseを読み込みます。")
        //  load config
        loadConfig()
        //  クラスローダー
        loadClass()
        //  Test Connect
        testConnect()
        //  Logger
        plugin.logger.info("DataBaseを読み込みました。")
    }

    fun loadConfig() {
        host = plugin.config.getString("database.host")
        user = plugin.config.getString("database.user")
        pass = plugin.config.getString("database.pass")
        port = plugin.config.getString("database.port")
        db = plugin.config.getString("database.db")
    }

    private fun loadClass() {
        try {
            Class.forName("com.mysql.jdbc.Driver")
            plugin.logger.info("Load class.")
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
            plugin.logger.info("DataBase connection class not found!")
        }
    }

    fun getConnection(): Connection? {
        val connection: Connection
        connection = try {
            DriverManager.getConnection("jdbc:mysql://$host:$port/$db?autoReconnect=true&useSSL=false&characterEncoding=latin1&useConfigs=maxPerformance", user, pass)
        } catch (e: SQLException) {
            e.printStackTrace()
            return null
        }
        return connection
    }

    private fun testConnect(): Boolean? {
        plugin.logger.info("接続テスト中....")
        if (getConnection() == null) {
            plugin.logger.info("接続に失敗しました。")
            return false
        }
        plugin.logger.info("接続に成功しました！")
        return true
    }

    fun sendErrorMessage() {
        plugin.logger.warning("接続エラーです。データベースがダウンしている、もしくはコネクションの設定を確認してください。")
        if (errorMode) return
        plugin.server.broadcastMessage("${prefix}§c§lデータベースの接続エラーです。運営にお知らせください。")
        errorMode = true
    }
}