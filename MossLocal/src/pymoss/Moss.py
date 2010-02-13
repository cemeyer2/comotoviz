from pymoss import Config
import elixir
'''
Created on Feb 11, 2010

@author: Charlie Meyer 
'''

def initializeDB(config=Config("/etc/pymoss.cfg")):
    elixir.metadata.bind=config.getDatabaseUrl()
    elixir.setup_all(True)
    
def dropDB(config=Config("/etc/pymoss.cfg")):
    elixir.metadata.bind=config.getDatabaseUrl()
    elixir.drop_all()

if __name__ == '__main__':
    pass