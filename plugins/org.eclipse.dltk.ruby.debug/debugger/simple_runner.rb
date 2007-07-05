###############################################################################
# Copyright (c) 2005, 2007 IBM Corporation and others.
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#

###############################################################################    

require 'dbgp/managers/log_null'
require 'dbgp/managers/log_file'
require 'dbgp/managers/log_stdout'

require 'simple_debugger'
                                           
module XoredDebugger
    
    class Runner
        def Runner.go
            log = ENV['DBGP_RUBY_LOG']
            logger = log.nil? ? NullLogManager.new : (log == 'stdout' ? StdoutLogManager.new : FileLogManager.new(log))

            host   = ENV['DBGP_RUBY_HOST']
            port   = ENV['DBGP_RUBY_PORT'].to_i
            key    = ENV['DBGP_RUBY_KEY']
            script = ENV['DBGP_RUBY_SCRIPT']
            test   = ENV['DBGP_RUBY_TEST']
            test   = test.nil? ? false : test == '1' ? true : false

            begin
                if (host.nil? or port == 0 or key.nil? or script.nil?)
                    logger.puts('Invalid debugger params')
                else
                    logger.puts("Time:   #{Time.new.to_s}")
                    logger.puts("Host:   #{host.to_s}")
                    logger.puts("Port:   #{port.to_s}")
                    logger.puts("Key:    #{key.to_s}")
                    logger.puts("Script: #{script.to_s}")
                    logger.puts("Test:   #{test.to_s}")

                    logger.puts('Include paths:')
                    $:.each { |path|
                        logger.puts("\t#{path}")
                    }


                    # Debugger setup
                    logger.puts('Creating debugger...')
                    debugger = RubyDebugger.new(host, port, key, script, logger, test)

                    logger.puts('Setting trace_func...')
                    set_trace_func proc { |event, file, line, id, binding, klass, *rest|
                        #logger.puts("=> Trace: #{event.to_s} from #{file.to_s} at #{line.to_s}")
                        debugger.trace(event, file, line, id, binding, klass)
                    }

                    # Script for debug
                    load script

                    # Debugger teardown
                    set_trace_func nil
                    debugger.terminate
                end

            rescue Exception
                logger.puts('Exception during debugging:')
                logger.puts("\tMessage: " + $!.message)
                logger.puts("\tBacktrace: " + $!.backtrace.join("\n"))
            ensure
                logger.close
            end  
        end # go
    end # class Runner

    Runner.go

end # XoredDebugger