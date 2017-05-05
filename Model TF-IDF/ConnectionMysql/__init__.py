#coding: utf-8

import MySQLdb

def conexaoMysql():
    db = MySQLdb.connect("localhost","SemanticWeb","123", "Features_Completo2")
    return db