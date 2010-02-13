import ConfigParser, os

'''
Created on Feb 11, 2010

@author: Charlie Meyer <cemeyer2@illinois.edu>
'''

class Config(object):
    '''
    classdocs
    '''


    def __init__(self, configPath):
        '''
        Constructor
        '''
        self.parser = ConfigParser.ConfigParser()
        self.parser.read([configPath, os.path.expanduser('~/pymoss.cfg')])
        
        
    def getDatabaseUrl(self):
        '''
        Returns the database url from the configuration file
        '''
        return self.parser.get('db', 'url')
    
    def getMossPath(self):
        return self.parser.get('moss', 'path')