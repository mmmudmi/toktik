import mariadb
from setting import get_db_config
def getConnection():
    return mariadb.connect(
        user=get_db_config("user"),
        password=get_db_config("passsword"),
        host=get_db_config("host"),
        port=get_db_config("port"),
        database=get_db_config("database")
    )