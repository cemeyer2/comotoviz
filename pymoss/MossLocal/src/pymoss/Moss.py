import elixir
from pymoss.config import Config
from pymoss.model import *
'''
Created on Feb 11, 2010

@author: Charlie Meyer 
'''

def initialize_db(config=Config("/etc/pymoss.cfg"), echo=False):
    elixir.metadata.bind=str(config.getDatabaseUrl())
    __set_db_echo(echo)
    elixir.setup_all(True)
    
def drop_db(config=Config("/etc/pymoss.cfg"), echo=False):
    elixir.metadata.bind=config.getDatabaseUrl()
    __set_db_echo(echo)
    elixir.drop_all()
    
def __set_db_echo(echo=False):
    elixir.metadata.bind.echo=echo

if __name__ == '__main__':
    initialize_db(echo=True)