import string

class ReadList(object):
 
   def load_inputpath(self, taskfile):

      pathlist = []
      f = open(taskfile)
      lines = f.readlines()
      f.close()
      
      for line in lines:

         if line.startswith('#'):
            continue

         line = line.strip('\n')
         pathlist.append(line)

      return pathlist 
      
   def load_inputpathString(self, taskfile):

      pathlist = self.load_inputpath(taskfile)
      ss = string.join(pathlist, ',')
      return ss
        
if __name__ == '__main__':
   readlist = ReadList()
   readlist.load_inputpathString('task.lst')
       
