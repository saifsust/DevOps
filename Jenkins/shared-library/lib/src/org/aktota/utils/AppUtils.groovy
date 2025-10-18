package org.aktota.utils

class AppUtils implements Serializable {
    def getAppNames() {
        return applications().keySet().toList()
    }

    def applications() {
        return [
                'mock-server': 'https://github.com/saifsust/mock-server.git',
                'workspace'  : 'https://github.com/saifsust/workspace.git'
        ]
    }
}
