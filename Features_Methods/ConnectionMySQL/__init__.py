import pymysql


def conexaoMysql():
    db = pymysql.connect("localhost","SemanticWeb","123", "Features_Completo2")
    return db