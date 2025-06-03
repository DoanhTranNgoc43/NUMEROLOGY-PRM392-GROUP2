using Numerology.Core.Constants;

namespace Numerology.Core.Models
{
    public class Response
    {
        public string Status { get; set; } = ResponseStatus.SUCCESS;
        public object? Message { get; set; }
        public object? Data { get; set; }
    }
}