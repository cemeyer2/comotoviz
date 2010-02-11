try:
    from setuptools import setup, find_packages
except ImportError:
    from ez_setup import use_setuptools
    use_setuptools()
    from setuptools import setup, find_packages

setup(
      # basic package data
      name = "pymoss",
      version = "0.1a",
      description='Python library for interacting with Moss',
      author='Charlie Meyer',
      author_email='cemeyer2@illinois.edu',
      url='https://maggie.cs.illinois.edu',
      
      # package structure
      packages=find_packages('src'),
      package_dir={'':'src'},
      
      install_requires = [
            'sqlalchemy',
            'elixir',
            'BeautifulSoup',
            'pysvn', 
            'optparse',
            'pygraphviz',
            ],
)