import sys
class ArgList(object):
   
   num_map = 'nm'
   is_num_map = False
   num_reduce = 'nr'
   is_num_reduce = False
   input_path = 'ip'
   is_input_path = False
   output_path = 'op'
   is_output_path = False
   input_list = 'il'
   is_input_list = False
   jar_file = 'jar'
   is_jar_file = False
   aid = 'aid'
   is_aid = False
   props = 'props'
   is_props = False
   
   
   def parseArgs(self, args):
      args_map = {}
      for arg in args:
        
         if (arg.strip() == ''):
            continue
 
         if (arg.endswith('.py')):
            continue      
   
         index = arg.find('=')
         if (index == -1):
            print 'invalid arg : '+ arg
            continue

         value = arg[index+1:]
         if (value == ''):
            continue

         if arg.startswith('--nm'):
            args_map[self.num_map] = value
            self.is_num_map = True

         elif arg.startswith('--nr'):
            args_map[self.num_reduce] = value
            self.is_num_reduce = True

         elif arg.startswith('--ip'):
            args_map[self.input_path] = value
            self.is_input_path = True

         elif arg.startswith('--op'):
            args_map[self.output_path] = value
            self.is_output_path = True

         elif arg.startswith('--il'):
            args_map[self.input_list] = value
            self.is_input_list = True

         elif arg.startswith('--jar'):
            args_map[self.jar_file] = value
            self.is_jar_file = True
             
         elif arg.startswith('--aid'):
            args_map[self.aid] = value
            self.is_aid = True

         elif arg.startswith('--props'):
            args_map[self.props] = value
            self.is_props = True

         else:
            print 'unknow arg : ' + arg

      return args_map

if __name__=='__main__':
   args = sys.argv
   arglist = ArgList()
   print arglist.parseArgs(args)

