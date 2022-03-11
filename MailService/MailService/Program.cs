using System.Net;
using System.Net.Mail;
using System.Text;



namespace BasicServerHTTPlistener
{
    enum Status
    {
        OK,
        Failed
    }
    internal class Program
    {
        public static void Main(string[] args)
        {
            Server server = new Server();
            server.Start("http://localhost:9090/");

        }

    }

    class Server
    {
        public static bool Terminate = false;

        public void Start(string uri)
        {
            if (!HttpListener.IsSupported)
            {
                Console.WriteLine("A more recent Windows version is required to use the HttpListener class.");
                return;
            }

            HttpListener listener = new HttpListener();

            listener.Prefixes.Add(uri);
            // don't forget to authorize access to the TCP/IP addresses localhost:xxxx and localhost:yyyy 
            // with netsh http add urlacl url=http://localhost:xxxx/ user="Tout le monde"
            // and netsh http add urlacl url=http://localhost:yyyy/ user="Tout le monde"
            // user="Tout le monde" is language dependent, use user=Everyone in english 

            listener.Start();

            Console.WriteLine("Listening for connections on " + uri);
            Console.WriteLine("Press Ctrl-C to stop the service");
            Console.WriteLine("Press the Space bar to stop / resume the web server");

            // Trap Ctrl-C on console to exit 
            Console.CancelKeyPress += delegate
            {
                listener.Stop();
                listener.Close();
                Environment.Exit(0);
            };

            var taskKeys = new Task(ReadKeys);
            taskKeys.Start();

            while (true)
            {
                // Note: The GetContext method blocks while waiting for a request.
                HttpListenerContext context = listener.GetContext();
                if (!Terminate)
                {
                    ProcessRequest(context);
                    Console.WriteLine("Processing request");
                }
            }
        }

        private void ReadKeys()
        {
            ConsoleKeyInfo key = new ConsoleKeyInfo();
            int count = 0;

            while (!Console.KeyAvailable && key.Key != ConsoleKey.Escape)
            {
                key = Console.ReadKey(true);

                switch (key.Key)
                {
                    case ConsoleKey.Spacebar:
                        if (count%2 == 0)
                        {
                            Console.WriteLine("Web server stopped.");
                        }
                        else
                        {
                            Console.WriteLine("Web server resumed.");
                        }
                        Terminate = true;
                        count++;
                        break;
                }
            }
        }

        public void ProcessRequest(HttpListenerContext context)
        {
            HttpListenerRequest request = context.Request;
            Console.WriteLine(request.Url.LocalPath);

            //Detect Http Method
            if (request.HttpMethod == "POST" && request.Url.LocalPath == "/send")
            {
                Console.WriteLine("POST received. Mail will be posted asap");

                string documentContents;
                using (Stream receiveStream = request.InputStream)
                {
                    using (StreamReader readStream = new StreamReader(receiveStream, Encoding.UTF8))
                    {
                        documentContents = readStream.ReadToEnd();
                    }
                }

                Console.WriteLine(documentContents);

                HttpListenerResponse response = context.Response;

                var smtpClient = new SmtpClient("smtp.gmail.com")
                {
                    Port = 587,
                    Credentials = new NetworkCredential("stonksdev.polyevent@gmail.com", "rpuzwenzijatkrfm"),
                    EnableSsl = true,
                };

                Status result = Status.OK;

                try
                {
                    smtpClient.Send("stonksdev.polyevent@gmail.com", "vincetl74@gmail.com", "object", documentContents);
                }
                catch (Exception ex)
                {
                    Console.WriteLine("Exception caught while sending the mail: {0}", ex.ToString());
                    result = Status.Failed;
                    
                }

                byte[] buffer = System.Text.Encoding.UTF8.GetBytes(result.ToString());
                response.ContentLength64 = buffer.Length;
                System.IO.Stream output = response.OutputStream;
                output.Write(buffer, 0, buffer.Length);
                output.Close();
            }
        }
    }
}