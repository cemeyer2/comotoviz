from setuptools import setup, find_packages

setup(
      # basic package data
      name = "Moss",
      version = "0.1a",
      
      # package structure
      packages=find_packages('src'),
      package_dir={'':'src'},
)