#!/usr/bin/env python

import os, tempfile, elixir, random, shutil, subprocess, BeautifulSoup, re
#elixir.metadata.bind = "sqlite:///db"
elixir.metadata.bind = "mysql://conrad:conrad@localhost/conrad"
elixir.metadata.bind.echo = True
from conrad.model import *
from sqlalchemy.orm.exc import NoResultFound

def main():
	elixir.setup_all(True)

	#quadtree = Assignment.get(('cs225', 'linkedlists'))
	imgmanip = Assignment.get(('cs225', 'imagemanipulation'))	
	ae = imgmanip.analysis
	me = ae.mossAnalysis
	print ae, me

	resultsStream = open(os.path.join(me.workDirectory, 'index.html'))
	soup = BeautifulSoup.BeautifulSoup(resultsStream.read())
	resultsStream.close()

	for row in soup.find('table').findAll('tr'):
		if row.find('th'):
			continue

		studentData = row.findAll('a', limit=2)
		text1 = studentData[0].contents[0]
		text2 = studentData[1].contents[0]
		link = studentData[0]['href']

		# 0/ (12%)
		textExp = re.compile(r'(\w+)/ \((\d+)%\)')
		(pseudonym1, score1) = textExp.match(text1).group(1, 2)
		(pseudonym2, score2) = textExp.match(text2).group(1, 2)

		submission1 = AnalysisPseudonym.get((imgmanip.analysis.id, pseudonym1)).submission
		submission2 = AnalysisPseudonym.get((imgmanip.analysis.id, pseudonym2)).submission

		print submission1, submission2, score1, score2, me

		match = MossMatch(submission1=submission1, score1=int(score1),
				  submission2=submission2, score2=int(score2),
				  link=link,
				  mossAnalysis=me)

		print match

	elixir.session.commit()

if __name__ == '__main__':
	main()
