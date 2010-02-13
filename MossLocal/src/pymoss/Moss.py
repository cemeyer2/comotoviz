from pymoss.config import Config
import elixir
'''
Created on Feb 11, 2010

@author: Charlie Meyer 
'''

def initializeDB(config=Config()):
    elixir.metadata.bind=config.getDatabaseUrl()

if __name__ == '__main__':
    pass