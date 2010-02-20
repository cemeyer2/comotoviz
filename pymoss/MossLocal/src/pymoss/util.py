import pysvn

'''
Created on Feb 13, 2010

@author: Charlie Meyer <cemeyer2@illinois.edu>
'''
#originally written by alambert
def get_roster(svnRoot):
        client = pysvn.Client()
        pathsListing = [result[0].repos_path.replace('/', '')
                        for result in client.list(svnRoot, recurse=False)]
        netids = filter(lambda x: not x.startswith('_') and len(x) > 0,
                        pathsListing)
        return netids