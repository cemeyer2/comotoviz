#!/usr/bin/env python

from Moss.model.model import *
from sqlalchemy.orm.exc import NoResultFound
import os
import elixir
#elixir.metadata.bind = "sqlite:///db"
elixir.metadata.bind = "mysql://conrad:conrad@localhost/conrad"
#elixir.metadata.bind.echo = True

def main():
	#elixir.metadata.bind = "sqlite:///db"
	#elixir.metadata.bind.echo = True
	elixir.setup_all(True)
	
	#cs225 = Course.get('cs225')
        #cs225sp09 = cs225.offerings[0];
	#cs225fa08 = cs225.offerings[1];
	#cs225fa09 = cs225.offerings[2];
	#mosaic = Assignment.get(('cs225', 'photomosaic'))
	
	cs225 = Course(name='cs225')
	sp09 = Semester(season=u'Spring', year=2009)
	fa08 = Semester(season=u'Fall', year=2008)
	fa09 = Semester(season=u'Fall', year=2009)
	sp10 = Semester(season=u'Spring', year=2010)

	cs225sp09 = Offering(semester=sp09, course=cs225)
	cs225fa08 = Offering(semester=fa08, course=cs225)
	cs225fa09 = Offering(semester=fa09, course=cs225)
	cs225sp10 = Offering(semester=sp10, course=cs225)

	imgmanip = Assignment(name='imagemanipulation', course=cs225, language='cpp')
	#imgmanip2 = Assignment(name='imagemanipulation2', course=cs225, language='cpp')
	#linkedlists = Assignment(name='linkedlists', course=cs225, language='cpp')
	#mazes = Assignment(name='mazes', course=cs225, language='cpp')
	#mosaic = Assignment(name='photomosaic', course=cs225, language='cpp')
	#stacks = Assignment(name='stacksandqueues', course=cs225, language='cpp')
	#quadtree = Assignment(name='quadtree', course=cs225, language='cpp')

	addfiles('fa08', cs225fa08, imgmanip, 'mp1', ['main.cpp', 'Makefile'])
	addfiles('sp09', cs225sp09, imgmanip, 'mp1', ['main.cpp', 'Makefile'])
	addfiles('fa09', cs225fa09, imgmanip, 'mp1', ['main.cpp', 'Makefile'])
	addfiles('sp10', cs225sp10, imgmanip, 'mp1', ['main.cpp', 'Makefile'])
	addsoln('soln/imgmanip', imgmanip, ['main.cpp', 'Makefile'])

	#addfiles('fa08', cs225fa08, imgmanip2, 'mp2', ['Image.cpp', 'Image.h', 'Scene.h', 'Scene.cpp', 'Makefile'])
	#addfiles('sp09', cs225sp09, imgmanip2, 'mp2', ['image.cpp', 'image.h', 'scene.h', 'scene.cpp', 'Makefile'])
	#addfiles('fa09', cs225fa09, imgmanip2, 'mp2', ['image.cpp', 'image.h', 'scene.h', 'scene.cpp', 'Makefile'])
	#addsoln('soln/imgmanip2/1', imgmanip2, ['image.cpp', 'image.h', 'scene.h', 'scene.cpp', 'Makefile'])
	#addsoln('soln/imgmanip2/2', imgmanip2,['Image.cpp', 'Image.h', 'Scene.h', 'Scene.cpp', 'Makefile'])

	#addfiles('fa08', cs225fa08, linkedlists, 'mp3', ['mp3extras.cpp'])
	#addfiles('sp09', cs225sp09, linkedlists, 'mp3', ['mp3extras.cpp'])
	#addfiles('fa09', cs225fa09, linkedlists, 'mp3', ['mp3extras.cpp'])
	#addsoln('soln/linkedlists', linkedlists, ['mp3extras.cpp'])

	#addfiles('fa08', cs225fa08, stacks, 'mp4', ['stack.cpp', 'queue.cpp', 'parser.cpp', 'parser.h', 'lexer.cpp', 'lexer.h'])
	#addfiles('sp09', cs225sp09, stacks, 'mp4', ['stack.cpp', 'queue.cpp', 'parser.cpp', 'parser.h', 'lexer.cpp', 'lexer.h'])
	#addfiles('fa09', cs225fa09, stacks, 'mp4', ['stack.cpp', 'queue.cpp', 'parser.cpp', 'parser.h', 'lexer.cpp', 'lexer.h'])
	#addsoln('soln/stacks', stacks, ['stack.cpp', 'queue.cpp', 'parser.cpp', 'parser.h', 'lexer.cpp', 'lexer.h'])

	#addfiles('fa08', cs225fa08, quadtree, 'mp5', ['quadtree.cpp', 'quadtree.h'])
	#addfiles('sp09', cs225sp09, quadtree, 'mp5', ['quadtree.cpp', 'quadtree.h'])
	#addfiles('fa09', cs225fa09, quadtree, 'mp5', ['quadtree.cpp', 'quadtree.h'])
	#addsoln('soln/quadtree', quadtree, ['quadtree.cpp', 'quadtree.h'])

	#addfiles('fa08', cs225fa08, mosaic, 'mp6', ['kdtilemapper.cpp', 'kdtilemapper.h', 'kdtree.cpp', 'kdtree.h'])
	#addfiles('sp09', cs225sp09, mosaic, 'mp6', ['kdtilemapper.cpp', 'kdtilemapper.h', 'kdtree.cpp', 'kdtree.h'])
	#addfiles('fa09', cs225fa09, mosaic, 'mp6', ['kdtilemapper.cpp', 'kdtilemapper.h', 'kdtree.cpp', 'kdtree.h'])
	#addsoln('soln/mosaic/1', mosaic, ['kdtilemapper.cpp', 'kdtilemapper.h', 'kdtree.cpp', 'kdtree.h'])
	#addsoln('soln/mosaic/2', mosaic, ['kdtilemapper.cpp', 'kdtilemapper.h', 'kdtree.cpp', 'kdtree.h'])

	#addfiles('fa08', cs225fa08, mazes, 'mp7', ['dsets.cpp', 'dsets.h', 'maze.cpp', 'maze.h'])
	#addfiles('sp09', cs225sp09, mazes, 'mp7', ['dsets.cpp', 'dsets.h', 'maze.cpp', 'maze.h'])
	#addfiles('fa09', cs225fa09, mazes, 'mp7', ['dsets.cpp', 'dsets.h', 'maze.cpp', 'maze.h'])
	#addsoln('soln/mazes/1', mazes, ['dsets.cpp', 'dsets.h', 'maze.cpp', 'maze.h'])
	#addsoln('soln/mazes/2',mazes, ['dsets.cpp', 'dsets.h', 'maze.cpp', 'maze.h'])
	#addsoln('soln/mazes/3',mazes, ['dsets.cpp', 'dsets.h', 'maze.cpp', 'maze.h'])

	elixir.session.commit()

def addsoln(basedir, assignment, files):
	sub = SolutionSubmission(assignment=assignment)
	for filename in files:
		content = tryread(os.path.join(basedir, filename))
		if content == None:
			raise Exception(filename)
		sub.submissionFiles.append(SubmissionFile(name=filename, content=content, meta=False))

def addfiles(basedir, offering, assignment, mpdir, files):
	for name in os.listdir(basedir):
		student = Student.query.get(name)
		if not student:
			student = Student(netid=name)

		sub = StudentSubmission(offering=offering, assignment=assignment, student=student)

		for filename in files:
			content = tryread(os.path.join(basedir, name, mpdir, filename))
			if not content == None:
				sub.submissionFiles.append(SubmissionFile(name=filename, content=content, meta=False))

		pcontent = tryread(os.path.join(basedir, name, mpdir, 'partners.txt'))
		if not pcontent == None:
			#print pcontent
			sub.submissionFiles.append(SubmissionFile(name='partners.txt', content=pcontent, meta=True))

def tryread(filename):
	try:
		f = open(filename, 'r')
		content = f.read()
		f.close()
		return content
	except:
		return None

if __name__ == '__main__':
	main()
