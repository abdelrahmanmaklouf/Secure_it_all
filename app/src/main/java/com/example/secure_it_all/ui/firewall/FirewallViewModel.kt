package com.example.secure_it_all.ui.firewall

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.secure_it_all.data.database.AppDatabase
import com.example.secure_it_all.data.database.FirewallRuleEntity
import com.example.secure_it_all.monitoring.AppScanner
import com.example.secure_it_all.data.models.AppInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import com.example.secure_it_all.firewall.FirewallEngine
import com.example.secure_it_all.firewall.FirewallResult
import com.example.secure_it_all.firewall.FirewallDecision
import com.example.secure_it_all.data.database.NetworkConnectionEntity
import com.example.secure_it_all.data.database.SecurityEventEntity
import com.example.secure_it_all.data.models.NetworkConnection
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import com.example.secure_it_all.data.repository.NetworkConnectionRepository
import com.example.secure_it_all.data.database.AppInfoEntity

class FirewallViewModel(app: Application) : AndroidViewModel(app) {

    private val db = AppDatabase.getInstance(app)
    private val dao = db.firewallRuleDao()

    val rules: Flow<List<FirewallRuleEntity>> = dao.getAllRules()

    val installedApps: List<AppInfo> by lazy {
        AppScanner(app).scanInstalledApps()
    }

    fun addAppRule(packageName: String, block: Boolean) {
        viewModelScope.launch {
            dao.insert(
                FirewallRuleEntity(
                    targetType = "APP",
                    target = packageName,
                    action = if (block) "BLOCK" else "ALLOW"
                )
            )
        }
    }

    fun addTestBlockRule() {
        addAppRule("com.suspicious.testapp", block = true)
    }
    fun addTestAppData() {
        viewModelScope.launch {
            db.appInfoDao().insertAll(
                listOf(
                    AppInfoEntity(
                        packageName = "com.suspicious.testapp",
                        appName = "PhotoBlur Pro",
                        installTime = System.currentTimeMillis(),
                        installerSource = "unknown",
                        dangerousPermissionsCount = 4,
                        permissionsCsv = "CAMERA,RECORD_AUDIO,ACCESS_FINE_LOCATION,READ_CONTACTS"
                    ),
                    AppInfoEntity(
                        packageName = "com.fastvpn.free",
                        appName = "Fast VPN Free",
                        installTime = System.currentTimeMillis(),
                        installerSource = "unknown",
                        dangerousPermissionsCount = 2,
                        permissionsCsv = "ACCESS_FINE_LOCATION,READ_SMS"
                    ),
                    AppInfoEntity(
                        packageName = "com.coupon.saver",
                        appName = "Coupon Saver",
                        installTime = System.currentTimeMillis(),
                        installerSource = "unknown",
                        dangerousPermissionsCount = 3,
                        permissionsCsv = "READ_CONTACTS,CAMERA,SEND_SMS"
                    )
                )
            )
        }
    }
    fun addDomainRule(domain: String, block: Boolean) {
        viewModelScope.launch {
            dao.insert(
                FirewallRuleEntity(
                    targetType = "DOMAIN",
                    target = domain,
                    action = if (block) "BLOCK" else "ALLOW"
                )
            )
        }
    }

    fun deleteRule(rule: FirewallRuleEntity) {
        viewModelScope.launch {
            dao.delete(rule)
        }
    }

    private val engine = FirewallEngine()
    private val loggedBlockIds = mutableSetOf<Long>()

    val connectionsWithDecision: Flow<List<Pair<NetworkConnectionEntity, FirewallResult>>> =
        combine(rules, db.networkConnectionDao().getRecentConnections()) { ruleList, connections ->
            engine.updateRules(ruleList)
            connections.map { entity ->
                val conn = NetworkConnection(
                    uid = entity.uid,
                    packageName = entity.packageName,
                    timestamp = entity.timestamp,
                    protocol = entity.protocol,
                    sourcePort = 0,
                    destinationIp = entity.destinationIp,
                    destinationPort = entity.destinationPort,
                    domain = entity.domain,
                    bytesSent = entity.bytesSent,
                    bytesReceived = entity.bytesReceived
                )
                val result = engine.evaluate(conn)

                if (result.decision == FirewallDecision.BLOCK && entity.id !in loggedBlockIds) {
                    loggedBlockIds.add(entity.id)
                    viewModelScope.launch {
                        db.securityEventDao().insert(
                            SecurityEventEntity(
                                timestamp = entity.timestamp,
                                source = "Firewall",
                                packageName = entity.packageName,
                                eventType = "Connection Blocked",
                                destination = entity.domain ?: entity.destinationIp,
                                severity = "HIGH"
                            )
                        )
                    }
                }

                entity to result
            }
        }

    private val networkRepo = NetworkConnectionRepository(db.networkConnectionDao())

    fun startTestData() {
        viewModelScope.launch {
            networkRepo.startMockCollection()
        }
    }
}